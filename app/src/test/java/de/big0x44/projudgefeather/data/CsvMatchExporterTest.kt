package de.big0x44.projudgefeather.data

import de.big0x44.projudgefeather.model.MatchResult
import de.big0x44.projudgefeather.model.MatchResultRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.ByteArrayOutputStream

class CsvMatchExporterTest {

    private class FakeMatchResultRepository(initialMatches: List<MatchResult> = emptyList()) : MatchResultRepository {
        private val _matches = MutableStateFlow(initialMatches)
        override fun getAllMatches(): Flow<List<MatchResult>> = _matches
        override suspend fun saveMatch(match: MatchResult) {
            val list = _matches.value.toMutableList()
            list.add(match)
            _matches.value = list
        }
        override suspend fun saveAll(matches: List<MatchResult>) {
            val list = _matches.value.toMutableList()
            list.addAll(matches)
            _matches.value = list
        }
        override suspend fun clearAllMatches() {
            _matches.value = emptyList()
        }
    }

    @Test
    fun testExportToCsv() = runBlocking {
        val matches = listOf(
            MatchResult(
                player1Name = "John \"The Pro\" Doe",
                player2Name = "Alice, Bob & Co.",
                score1 = 21,
                score2 = 19,
                timestamp = 1780000000000L,
                winnerName = "John \"The Pro\" Doe"
            )
        )
        val repo = FakeMatchResultRepository(matches)
        val exporter = CsvMatchExporter(repo, Dispatchers.Unconfined)

        val baos = ByteArrayOutputStream()
        exporter.exportToCsv(baos)
        val csvContent = baos.toString("UTF-8")

        // Assert header is present and fields are escaped properly
        assertTrue(csvContent.startsWith("player1,player2,score1,score2,timestamp,winner"))
        assertTrue(csvContent.contains("\"John \"\"The Pro\"\" Doe\""))
        assertTrue(csvContent.contains("\"Alice, Bob & Co.\""))
        assertTrue(csvContent.contains("21,19,1780000000000"))
    }
}
