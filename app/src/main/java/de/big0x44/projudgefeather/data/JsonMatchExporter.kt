package de.big0x44.projudgefeather.data

import de.big0x44.projudgefeather.di.IoDispatcher
import de.big0x44.projudgefeather.model.MatchResultRepository
import de.big0x44.projudgefeather.model.PlayerRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.io.PrintWriter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JsonMatchExporter @Inject constructor(
    private val matchResultRepository: MatchResultRepository,
    private val playerRepository: PlayerRepository,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun exportToJson(outputStream: OutputStream) {
        withContext(ioDispatcher) {
            val matches = matchResultRepository.getAllMatches().first()
            val players = playerRepository.getAllPlayers().first()
            
            val playersJson = players.joinToString(separator = ",") { player ->
                """{"id":"${escapeJson(player.id)}","name":"${escapeJson(player.name)}"}"""
            }
            
            val matchesJson = matches.joinToString(separator = ",") { match ->
                """{"id":"${escapeJson(match.id)}","player1Id":"${escapeJson(match.player1Id)}","player2Id":"${escapeJson(match.player2Id)}","score1":${match.score1},"score2":${match.score2},"timestamp":${match.timestamp},"winnerId":"${escapeJson(match.winnerId)}"}"""
            }
            
            val jsonPackage = """{"players":[$playersJson],"matches":[$matchesJson]}"""
            
            val writer = PrintWriter(OutputStreamWriter(outputStream, "UTF-8"))
            writer.print(jsonPackage)
            writer.flush()
        }
    }

    private fun escapeJson(value: String): String {
        return value.replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r")
            .replace("\t", "\\t")
    }
}
