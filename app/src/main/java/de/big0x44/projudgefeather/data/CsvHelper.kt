package de.big0x44.projudgefeather.data

import de.big0x44.projudgefeather.model.MatchResult
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.io.PrintWriter

object CsvHelper {
    private const val HEADER = "player1,player2,score1,score2,timestamp,winner"

    fun exportToCsv(matches: List<MatchResult>, outputStream: OutputStream) {
        val writer = PrintWriter(OutputStreamWriter(outputStream, "UTF-8"))
        writer.println(HEADER)
        for (match in matches) {
            val p1 = escape(match.player1Name)
            val p2 = escape(match.player2Name)
            val s1 = match.score1
            val s2 = match.score2
            val ts = match.timestamp
            val w = escape(match.winnerName)
            writer.println("$p1,$p2,$s1,$s2,$ts,$w")
        }
        writer.flush()
    }

    fun importFromCsv(inputStream: InputStream): List<MatchResult> {
        val reader = BufferedReader(InputStreamReader(inputStream, "UTF-8"))
        val results = mutableListOf<MatchResult>()
        var line = reader.readLine() // Skip header or process if not header
        if (line != null && !line.startsWith("player1")) {
            parseLine(line)?.let { results.add(it) }
        }
        while (reader.readLine().also { line = it } != null) {
            parseLine(line!!)?.let { results.add(it) }
        }
        return results
    }

    private fun escape(value: String): String {
        val needQuotes = value.contains(",") || value.contains("\"") || value.contains("\n") || value.contains("\r")
        val escaped = value.replace("\"", "\"\"")
        return if (needQuotes) "\"$escaped\"" else escaped
    }

    private fun parseLine(line: String): MatchResult? {
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
            } else if (c == ',' && !inQuotes) {
                cells.add(currentField.toString())
                currentField = StringBuilder()
            } else {
                currentField.append(c)
            }
            i++
        }
        cells.add(currentField.toString())

        if (cells.size < 6) return null
        return try {
            MatchResult(
                player1Name = cells[0],
                player2Name = cells[1],
                score1 = cells[2].toInt(),
                score2 = cells[3].toInt(),
                timestamp = cells[4].toLong(),
                winnerName = cells[5]
            )
        } catch (e: Exception) {
            null
        }
    }
}
