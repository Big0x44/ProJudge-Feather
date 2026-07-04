package de.big0x44.projudgefeather.data

import de.big0x44.projudgefeather.model.MatchResult
import de.big0x44.projudgefeather.model.MatchResultRepository
import de.big0x44.projudgefeather.model.Player
import de.big0x44.projudgefeather.model.PlayerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.ByteArrayOutputStream

class JsonMatchExporterTest {

    private class FakePlayerRepository(initialPlayers: List<Player> = emptyList()) : PlayerRepository {
        private val _players = MutableStateFlow(initialPlayers)
        override fun getAllPlayers(): Flow<List<Player>> = _players
        override suspend fun addPlayer(name: String): Boolean = true
        override suspend fun addPlayer(id: String, name: String): Boolean = true
        override suspend fun deletePlayer(player: Player) {}
    }

    private class FakeMatchResultRepository(initialMatches: List<MatchResult> = emptyList()) : MatchResultRepository {
        private val _matches = MutableStateFlow(initialMatches)
        override fun getAllMatches(): Flow<List<MatchResult>> = _matches
        override suspend fun saveMatch(match: MatchResult) {}
        override suspend fun saveAll(matches: List<MatchResult>) {}
        override suspend fun clearAllMatches() {}
    }

    @Test
    fun testExportToJson() = runBlocking {
        val players = listOf(
            Player(id = "p1", name = "Alice"),
            Player(id = "p2", name = "Bob")
        )
        val matches = listOf(
            MatchResult(
                id = "match-1",
                player1Id = "p1",
                player2Id = "p2",
                score1 = 21,
                score2 = 19,
                timestamp = 1780000000000L,
                winnerId = "p1"
            )
        )
        val playerRepo = FakePlayerRepository(players)
        val matchRepo = FakeMatchResultRepository(matches)
        val exporter = JsonMatchExporter(matchRepo, playerRepo, Dispatchers.Unconfined)

        val baos = ByteArrayOutputStream()
        exporter.exportToJson(baos)
        val jsonContent = baos.toString("UTF-8")

        // Assert JSON contains expected structure and data
        assertTrue(jsonContent.contains("\"players\":["))
        assertTrue(jsonContent.contains("\"matches\":["))
        assertTrue(jsonContent.contains("\"id\":\"p1\""))
        assertTrue(jsonContent.contains("\"name\":\"Alice\""))
        assertTrue(jsonContent.contains("\"id\":\"match-1\""))
        assertTrue(jsonContent.contains("\"score1\":21"))
        assertTrue(jsonContent.contains("\"score2\":19"))
    }
}
