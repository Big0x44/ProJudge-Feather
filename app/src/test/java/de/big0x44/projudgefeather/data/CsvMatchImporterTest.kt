package de.big0x44.projudgefeather.data

import de.big0x44.projudgefeather.model.MatchResult
import de.big0x44.projudgefeather.model.MatchResultRepository
import de.big0x44.projudgefeather.model.Player
import de.big0x44.projudgefeather.model.PlayerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.ByteArrayInputStream

class CsvMatchImporterTest {

    private class FakePlayerRepository : PlayerRepository {
        private val _players = MutableStateFlow<List<Player>>(emptyList())
        override fun getAllPlayers(): Flow<List<Player>> = _players
        override suspend fun addPlayer(name: String): Boolean {
            val list = _players.value.toMutableList()
            if (list.any { it.name == name }) return false
            list.add(Player(id = java.util.UUID.randomUUID().toString(), name = name))
            _players.value = list
            return true
        }
        override suspend fun addPlayer(id: String, name: String): Boolean {
            val list = _players.value.toMutableList()
            if (list.any { it.id == id }) return false
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
            id,player1,player2,score1,score2,timestamp,winner
            "match-uuid-1","player-uuid-1","player-uuid-2",21,19,1780000000000,"player-uuid-1"
        """.trimIndent()

        val repo = FakeMatchResultRepository()
        val playerRepo = FakePlayerRepository()
        val importer = CsvMatchImporter(repo, playerRepo, Dispatchers.Unconfined)

        val bais = ByteArrayInputStream(csvData.toByteArray(Charsets.UTF_8))
        importer.importFromCsv(bais)

        val importedMatches = repo.getAllMatches().first()
        assertEquals(1, importedMatches.size)
        val imported = importedMatches[0]
        
        assertEquals("match-uuid-1", imported.id)
        assertEquals("player-uuid-1", imported.player1Id)
        assertEquals("player-uuid-2", imported.player2Id)
        assertEquals(21, imported.score1)
        assertEquals(19, imported.score2)
        assertEquals(1780000000000L, imported.timestamp)
        assertEquals("player-uuid-1", imported.winnerId)
        
        val players = playerRepo.getAllPlayers().first()
        assertEquals(2, players.size)
        val p1 = players.find { it.id == "player-uuid-1" }
        val p2 = players.find { it.id == "player-uuid-2" }
        assertEquals("Player_player-u", p1?.name)
        assertEquals("Player_player-u", p2?.name)
    }
}
