package de.big0x44.projudgefeather.data

import de.big0x44.projudgefeather.model.MatchResult
import de.big0x44.projudgefeather.model.MatchResultRepository
import de.big0x44.projudgefeather.model.Player
import de.big0x44.projudgefeather.model.PlayerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.ByteArrayInputStream

class JsonMatchImporterTest {

    private class FakePlayerRepository(initialPlayers: List<Player> = emptyList()) : PlayerRepository {
        private val _players = MutableStateFlow(initialPlayers)
        override fun getAllPlayers(): Flow<List<Player>> = _players.asStateFlow()
        override suspend fun addPlayer(name: String): Boolean {
            val list = _players.value.toMutableList()
            if (list.any { it.name == name }) return false
            list.add(Player(id = java.util.UUID.randomUUID().toString(), name = name))
            _players.value = list
            return true
        }
        override suspend fun addPlayer(id: String, name: String): Boolean {
            val list = _players.value.toMutableList()
            if (list.any { it.id == id || it.name == name }) return false
            list.add(Player(id = id, name = name))
            _players.value = list
            return true
        }
        override suspend fun deletePlayer(player: Player) {
            val list = _players.value.toMutableList()
            list.remove(player)
            _players.value = list
        }
    }

    private class FakeMatchResultRepository : MatchResultRepository {
        private val _matches = MutableStateFlow<List<MatchResult>>(emptyList())
        override fun getAllMatches(): Flow<List<MatchResult>> = _matches.asStateFlow()
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
    fun testImportFromJson() = runBlocking {
        val jsonData = """
            {
              "players": [
                {"id":"player-1","name":"John Doe"},
                {"id":"player-2","name":"Alice Smith"}
              ],
              "matches": [
                {"id":"match-1","player1Id":"player-1","player2Id":"player-2","score1":21,"score2":19,"timestamp":1780000000000,"winnerId":"player-1"}
              ]
            }
        """.trimIndent()

        val matchRepo = FakeMatchResultRepository()
        val playerRepo = FakePlayerRepository()
        val importer = JsonMatchImporter(matchRepo, playerRepo, Dispatchers.Unconfined)

        val bais = ByteArrayInputStream(jsonData.toByteArray(Charsets.UTF_8))
        importer.importFromJson(bais)

        val importedMatches = matchRepo.getAllMatches().first()
        assertEquals(1, importedMatches.size)
        val imported = importedMatches[0]

        assertEquals("match-1", imported.id)
        assertEquals("player-1", imported.player1Id)
        assertEquals("player-2", imported.player2Id)
        assertEquals(21, imported.score1)
        assertEquals(19, imported.score2)
        assertEquals(1780000000000L, imported.timestamp)
        assertEquals("player-1", imported.winnerId)

        val players = playerRepo.getAllPlayers().first()
        assertEquals(2, players.size)
        val p1 = players.find { it.id == "player-1" }
        val p2 = players.find { it.id == "player-2" }
        assertEquals("John Doe", p1?.name)
        assertEquals("Alice Smith", p2?.name)
    }

    @Test
    fun testImportFromJsonWithDuplicatePlayerNames() = runBlocking {
        // Alice Smith already exists in database with a different ID
        val initialPlayers = listOf(
            Player(id = "existing-alice", name = "Alice Smith")
        )
        val playerRepo = FakePlayerRepository(initialPlayers)
        val matchRepo = FakeMatchResultRepository()

        // JSON file contains player with name "Alice Smith" but ID "player-2"
        val jsonData = """
            {
              "players": [
                {"id":"player-1","name":"John Doe"},
                {"id":"player-2","name":"Alice Smith"}
              ],
              "matches": [
                {"id":"match-1","player1Id":"player-1","player2Id":"player-2","score1":21,"score2":19,"timestamp":1780000000000,"winnerId":"player-1"}
              ]
            }
        """.trimIndent()

        val importer = JsonMatchImporter(matchRepo, playerRepo, Dispatchers.Unconfined)
        val bais = ByteArrayInputStream(jsonData.toByteArray(Charsets.UTF_8))
        importer.importFromJson(bais)

        // The imported match should reference "existing-alice" instead of "player-2" to prevent duplicate name
        val importedMatches = matchRepo.getAllMatches().first()
        assertEquals(1, importedMatches.size)
        val imported = importedMatches[0]
        assertEquals("player-1", imported.player1Id)
        assertEquals("existing-alice", imported.player2Id)

        val players = playerRepo.getAllPlayers().first()
        // No new Alice Smith should be created, only John Doe should be added
        assertEquals(2, players.size)
        assertTrue(players.any { it.id == "existing-alice" && it.name == "Alice Smith" })
        assertTrue(players.any { it.id == "player-1" && it.name == "John Doe" })
    }
}
