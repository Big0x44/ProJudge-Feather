package de.big0x44.projudgefeather.data

import de.big0x44.projudgefeather.di.IoDispatcher
import de.big0x44.projudgefeather.model.MatchResult
import de.big0x44.projudgefeather.model.MatchResultRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CsvMatchImporter @Inject constructor(
    private val matchResultRepository: MatchResultRepository,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun importFromCsv(inputStream: InputStream) {
        withContext(ioDispatcher) {
            val reader = BufferedReader(InputStreamReader(inputStream, "UTF-8"))
            val parsedRows = mutableListOf<MatchResult>()
            var line = reader.readLine() // Skip header or process if not header
            val firstColumnHeader = CsvFormat.HEADER.split(CsvFormat.DELIMITER)[0]
            if (line != null && !line.startsWith(firstColumnHeader)) {
                parseTempRow(line)?.let { parsedRows.add(it) }
            }
            while (reader.readLine().also { line = it } != null) {
                parseTempRow(line!!)?.let { parsedRows.add(it) }
            }
            
            if (parsedRows.isEmpty()) return@withContext

            matchResultRepository.saveAll(parsedRows)
        }
    }

    private fun parseTempRow(line: String): MatchResult? {
        if (line.trim().isEmpty()) return null
        val cells = mutableListOf<String>()
        var inQuotes = false
        var currentField = StringBuilder()
        var i = 0
        while (i < line.length) {
            val c = line[i]
            if (c == '"') {
                if (inQuotes && i + 1 < line.length && line[i + 1] == '"') {
                    currentField.append('"')
                    i++
                } else {
                    inQuotes = !inQuotes
                }
            } else if (c == CsvFormat.DELIMITER && !inQuotes) {
                cells.add(currentField.toString())
                currentField = StringBuilder()
            } else {
                currentField.append(c)
            }
            i++
        }
        cells.add(currentField.toString())

        if (cells.size < CsvFormat.EXPECTED_COLUMNS) return null
        return try {
            MatchResult(
                id = cells[CsvFormat.COL_ID],
                player1Id = cells[CsvFormat.COL_PLAYER1],
                player2Id = cells[CsvFormat.COL_PLAYER2],
                score1 = cells[CsvFormat.COL_SCORE1].toInt(),
                score2 = cells[CsvFormat.COL_SCORE2].toInt(),
                timestamp = cells[CsvFormat.COL_TIMESTAMP].toLong(),
                winnerId = cells[CsvFormat.COL_WINNER]
            )
        } catch (e: Exception) {
            null
        }
    }
}
