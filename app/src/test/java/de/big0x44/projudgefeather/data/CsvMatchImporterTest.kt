package de.big0x44.projudgefeather.data

import de.big0x44.projudgefeather.model.MatchResult
import de.big0x44.projudgefeather.model.MatchResultRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.ByteArrayInputStream

class CsvMatchImporterTest {

    private class FakeMatchResultRepository : MatchResultRepository {
        private val _matches = MutableStateFlow<List<MatchResult>>(emptyList())
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
    fun testImportFromCsv() = runBlocking {
        val csvData = """
            player1,player2,score1,score2,timestamp,winner
            "John ""The Pro"" Doe","Alice, Bob & Co.",21,19,1780000000000,"John ""The Pro"" Doe"
        """.trimIndent()

        val repo = FakeMatchResultRepository()
        val importer = CsvMatchImporter(repo, Dispatchers.Unconfined)

        val bais = ByteArrayInputStream(csvData.toByteArray(Charsets.UTF_8))
        importer.importFromCsv(bais)

        val importedMatches = repo.getAllMatches().first()
        assertEquals(1, importedMatches.size)
        val imported = importedMatches[0]
        assertEquals("John \"The Pro\" Doe", imported.player1Name)
        assertEquals("Alice, Bob & Co.", imported.player2Name)
        assertEquals(21, imported.score1)
        assertEquals(19, imported.score2)
        assertEquals(1780000000000L, imported.timestamp)
        assertEquals("John \"The Pro\" Doe", imported.winnerName)
    }
}
