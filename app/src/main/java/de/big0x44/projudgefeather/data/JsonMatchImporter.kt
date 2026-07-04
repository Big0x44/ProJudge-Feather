package de.big0x44.projudgefeather.data

import de.big0x44.projudgefeather.di.IoDispatcher
import de.big0x44.projudgefeather.model.MatchResult
import de.big0x44.projudgefeather.model.MatchResultRepository
import de.big0x44.projudgefeather.model.Player
import de.big0x44.projudgefeather.model.PlayerRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JsonMatchImporter @Inject constructor(
    private val matchResultRepository: MatchResultRepository,
    private val playerRepository: PlayerRepository,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun importFromJson(inputStream: InputStream) {
        withContext(ioDispatcher) {
            val reader = BufferedReader(InputStreamReader(inputStream, "UTF-8"))
            val jsonString = reader.use { it.readText() }
            if (jsonString.trim().isEmpty()) return@withContext

            val parsedData = try {
                SimpleJsonParser(jsonString).parse()
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext
            }

            @Suppress("UNCHECKED_CAST")
            val playersJson = parsedData["players"] as? List<Map<String, Any>> ?: emptyList()
            @Suppress("UNCHECKED_CAST")
            val matchesJson = parsedData["matches"] as? List<Map<String, Any>> ?: emptyList()

            // 1. Get existing database state
            val existingPlayers = playerRepository.getAllPlayers().first()
            val existingById = existingPlayers.map { it.id }.toSet()
            val existingByName = existingPlayers.associate { it.name to it.id }

            // 2. Perform ID mapping to avoid duplicate names
            val idMapping = mutableMapOf<String, String>()
            
            for (playerMap in playersJson) {
                val id = playerMap["id"] as? String ?: continue
                val name = playerMap["name"] as? String ?: continue
                
                if (id in existingById) {
                    idMapping[id] = id
                } else {
                    val existingId = existingByName[name]
                    if (existingId != null) {
                        idMapping[id] = existingId
                    } else {
                        playerRepository.addPlayer(id, name)
                        idMapping[id] = id
                    }
                }
            }

            // 3. Process matches with mapped IDs
            val matchesToSave = mutableListOf<MatchResult>()
            for (matchMap in matchesJson) {
                val id = matchMap["id"] as? String ?: continue
                val rawP1Id = matchMap["player1Id"] as? String ?: continue
                val rawP2Id = matchMap["player2Id"] as? String ?: continue
                val score1 = (matchMap["score1"] as? Number)?.toInt() ?: continue
                val score2 = (matchMap["score2"] as? Number)?.toInt() ?: continue
                val timestamp = (matchMap["timestamp"] as? Number)?.toLong() ?: continue
                val rawWinnerId = matchMap["winnerId"] as? String ?: continue

                val p1Id = idMapping[rawP1Id] ?: rawP1Id
                val p2Id = idMapping[rawP2Id] ?: rawP2Id
                val winnerId = idMapping[rawWinnerId] ?: rawWinnerId

                matchesToSave.add(
                    MatchResult(
                        id = id,
                        player1Id = p1Id,
                        player2Id = p2Id,
                        score1 = score1,
                        score2 = score2,
                        timestamp = timestamp,
                        winnerId = winnerId
                    )
                )
            }

            if (matchesToSave.isNotEmpty()) {
                matchResultRepository.saveAll(matchesToSave)
            }
        }
    }

    private class SimpleJsonParser(private val json: String) {
        private var pos = 0

        fun parse(): Map<String, Any> {
            skipWhitespace()
            return parseObject()
        }

        private fun parseObject(): Map<String, Any> {
            val map = mutableMapOf<String, Any>()
            consume('{')
            skipWhitespace()
            if (pos < json.length && json[pos] == '}') {
                consume('}')
                return map
            }
            while (pos < json.length) {
                skipWhitespace()
                val key = parseString()
                skipWhitespace()
                consume(':')
                skipWhitespace()
                val value = parseValue()
                map[key] = value
                skipWhitespace()
                if (pos < json.length && json[pos] == ',') {
                    consume(',')
                } else {
                    break
                }
            }
            consume('}')
            return map
        }

        private fun parseArray(): List<Any> {
            val list = mutableListOf<Any>()
            consume('[')
            skipWhitespace()
            if (pos < json.length && json[pos] == ']') {
                consume(']')
                return list
            }
            while (pos < json.length) {
                skipWhitespace()
                val value = parseValue()
                list.add(value)
                skipWhitespace()
                if (pos < json.length && json[pos] == ',') {
                    consume(',')
                } else {
                    break
                }
            }
            consume(']')
            return list
        }

        private fun parseValue(): Any {
            skipWhitespace()
            if (pos >= json.length) throw IllegalArgumentException("Unexpected EOF")
            val c = json[pos]
            return when (c) {
                '{' -> parseObject()
                '[' -> parseArray()
                '"' -> parseString()
                else -> parseNumberOrBoolean()
            }
        }

        private fun parseString(): String {
            consume('"')
            val sb = StringBuilder()
            while (pos < json.length) {
                val c = json[pos++]
                if (c == '"') {
                    return sb.toString()
                } else if (c == '\\' && pos < json.length) {
                    val next = json[pos++]
                    when (next) {
                        'n' -> sb.append('\n')
                        'r' -> sb.append('\r')
                        't' -> sb.append('\t')
                        'b' -> sb.append('\b')
                        'f' -> sb.append('\u000C')
                        else -> sb.append(next)
                    }
                } else {
                    sb.append(c)
                }
            }
            throw IllegalArgumentException("Unterminated string")
        }

        private fun parseNumberOrBoolean(): Any {
            val sb = StringBuilder()
            while (pos < json.length) {
                val c = json[pos]
                if (c.isLetterOrDigit() || c == '.' || c == '-' || c == '+') {
                    sb.append(c)
                    pos++
                } else {
                    break
                }
            }
            val str = sb.toString()
            if (str == "true") return true
            if (str == "false") return false
            if (str == "null") return Unit
            return try {
                if (str.contains('.')) str.toDouble() else str.toLong()
            } catch (e: Exception) {
                str
            }
        }

        private fun consume(expected: Char) {
            if (pos >= json.length || json[pos] != expected) {
                throw IllegalArgumentException("Expected '$expected' at position $pos, but got '${if (pos >= json.length) "EOF" else json[pos].toString()}'")
            }
            pos++
        }

        private fun skipWhitespace() {
            while (pos < json.length && json[pos].isWhitespace()) {
                pos++
            }
        }
    }
}
