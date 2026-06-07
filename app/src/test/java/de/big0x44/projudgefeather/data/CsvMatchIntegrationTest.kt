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
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class CsvMatchIntegrationTest {

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
    fun testExportAndImportCycle() = runBlocking {
        // 1. Prepare initial database data
        val sourcePlayerRepo = FakePlayerRepository()
        sourcePlayerRepo.addPlayer("player-id-1", "John Doe")
        sourcePlayerRepo.addPlayer("player-id-2", "Alice Smith")

        val match1 = MatchResult(
            id = "match-uuid-1",
            player1Id = "player-id-1",
            player2Id = "player-id-2",
            score1 = 21,
            score2 = 18,
            timestamp = 1680000000000L,
            winnerId = "player-id-1"
        )
        val match2 = MatchResult(
            id = "match-uuid-2",
            player1Id = "player-id-2",
            player2Id = "player-id-1",
            score1 = 15,
            score2 = 21,
            timestamp = 1680000060000L,
            winnerId = "player-id-1"
        )

        val sourceMatchRepo = FakeMatchResultRepository(listOf(match1, match2))

        // 2. Export matches using CsvMatchExporter
        val exporter = CsvMatchExporter(sourceMatchRepo, Dispatchers.Unconfined)
        val outputStream = ByteArrayOutputStream()
        exporter.exportToCsv(outputStream)
        val exportedBytes = outputStream.toByteArray()

        // Verify CSV structure contains expected rows
        val csvString = exportedBytes.toString(Charsets.UTF_8)
        assertTrue(csvString.startsWith("id,player1,player2,score1,score2,timestamp,winner"))
        assertTrue(csvString.contains("match-uuid-1,player-id-1,player-id-2,21,18,1680000000000,player-id-1"))
        assertTrue(csvString.contains("match-uuid-2,player-id-2,player-id-1,15,21,1680000060000,player-id-1"))

        // 3. Import matches into a completely new repository using CsvMatchImporter
        val targetMatchRepo = FakeMatchResultRepository()
        val targetPlayerRepo = FakePlayerRepository()
        val importer = CsvMatchImporter(targetMatchRepo, targetPlayerRepo, Dispatchers.Unconfined)
        
        val inputStream = ByteArrayInputStream(exportedBytes)
        importer.importFromCsv(inputStream)

        // 4. Assert that imported data matches exported data exactly
        val importedMatches = targetMatchRepo.getAllMatches().first()
        assertEquals(2, importedMatches.size)

        // Assert match 1 details
        val importedMatch1 = importedMatches.find { it.id == "match-uuid-1" }
        assertTrue(importedMatch1 != null)
        assertEquals("player-id-1", importedMatch1!!.player1Id)
        assertEquals("player-id-2", importedMatch1.player2Id)
        assertEquals(21, importedMatch1.score1)
        assertEquals(18, importedMatch1.score2)
        assertEquals(1680000000000L, importedMatch1.timestamp)
        assertEquals("player-id-1", importedMatch1.winnerId)

        // Assert match 2 details
        val importedMatch2 = importedMatches.find { it.id == "match-uuid-2" }
        assertTrue(importedMatch2 != null)
        assertEquals("player-id-2", importedMatch2!!.player1Id)
        assertEquals("player-id-1", importedMatch2.player2Id)
        assertEquals(15, importedMatch2.score1)
        assertEquals(21, importedMatch2.score2)
        assertEquals(1680000060000L, importedMatch2.timestamp)
        assertEquals("player-id-1", importedMatch2.winnerId)

        // Assert target players were correctly auto-populated
        val targetPlayers = targetPlayerRepo.getAllPlayers().first()
        assertEquals(2, targetPlayers.size)
        val p1 = targetPlayers.find { it.id == "player-id-1" }
        val p2 = targetPlayers.find { it.id == "player-id-2" }
        assertTrue(p1 != null)
        assertTrue(p2 != null)
        assertEquals("Player_player-i", p1!!.name)
        assertEquals("Player_player-i", p2!!.name)
    }
}
