package de.big0x44.projudgefeather.data

import de.big0x44.projudgefeather.di.IoDispatcher
import de.big0x44.projudgefeather.model.MatchResultRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.io.PrintWriter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CsvMatchExporter @Inject constructor(
    private val matchResultRepository: MatchResultRepository,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun exportToCsv(outputStream: OutputStream) {
        withContext(ioDispatcher) {
            val matches = matchResultRepository.getAllMatches().first()
            val writer = PrintWriter(OutputStreamWriter(outputStream, "UTF-8"))
            writer.println(CsvFormat.HEADER)
            for (match in matches) {
                val p1 = CsvFormat.escape(match.player1Name)
                val p2 = CsvFormat.escape(match.player2Name)
                val s1 = match.score1
                val s2 = match.score2
                val ts = match.timestamp
                val w = CsvFormat.escape(match.winnerName)
                writer.println("$p1,$p2,$s1,$s2,$ts,$w")
            }
            writer.flush()
        }
    }
}
