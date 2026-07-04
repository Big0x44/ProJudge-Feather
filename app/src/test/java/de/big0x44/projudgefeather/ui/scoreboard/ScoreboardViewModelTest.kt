package de.big0x44.projudgefeather.ui.scoreboard

import de.big0x44.projudgefeather.data.CsvMatchExporter
import de.big0x44.projudgefeather.data.CsvMatchImporter
import de.big0x44.projudgefeather.model.MatchResult
import de.big0x44.projudgefeather.model.MatchResultRepository
import de.big0x44.projudgefeather.model.MatchStatus
import de.big0x44.projudgefeather.model.Player
import de.big0x44.projudgefeather.model.PlayerRepository
import de.big0x44.projudgefeather.model.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class FakePlayerRepository : PlayerRepository {
    private val _players = MutableStateFlow<List<Player>>(emptyList())
    override fun getAllPlayers(): Flow<List<Player>> = _players
    override suspend fun addPlayer(name: String): Boolean {
        val list = _players.value.toMutableList()
        list.add(Player(id = (list.size + 1).toString(), name = name))
        _players.value = list
        return true
    }
    override suspend fun addPlayer(id: String, name: String): Boolean {
        val list = _players.value.toMutableList()
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

class FakeMatchResultRepository : MatchResultRepository {
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

class ScoreboardViewModelTest {

    private lateinit var viewModel: ScoreboardViewModel
    private lateinit var playerRepository: FakePlayerRepository
    private lateinit var matchResultRepository: FakeMatchResultRepository
    private lateinit var settingsRepository: SettingsRepository

    class FakeSettingsRepository(initialPoints: Int = 21) : SettingsRepository {
        private val _winningPoints = MutableStateFlow(initialPoints)
        override val winningPoints = _winningPoints.asStateFlow()
        override fun setWinningPoints(points: Int) {
            _winningPoints.value = points
        }
    }

    @Before
    fun setUp() {
        playerRepository = FakePlayerRepository()
        matchResultRepository = FakeMatchResultRepository()
        settingsRepository = FakeSettingsRepository()
        val testScope = kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Unconfined)
        val testDispatcher = kotlinx.coroutines.Dispatchers.Unconfined
        val csvMatchExporter = CsvMatchExporter(matchResultRepository, testDispatcher)
        val csvMatchImporter = CsvMatchImporter(matchResultRepository, playerRepository, testDispatcher)
        viewModel = ScoreboardViewModel(
            playerRepository,
            matchResultRepository,
            csvMatchExporter,
            csvMatchImporter,
            settingsRepository,
            testScope,
            testDispatcher
        )
    }

    @Test
    fun testInitialState() {
        val state = viewModel.uiState.value
        assertEquals(0, state.score1)
        assertEquals(0, state.score2)
        assertNull(state.name1) // Null represents fallback to localized default
        assertNull(state.name2)
        assertNull(state.status1)
        assertNull(state.status2)
        assertFalse(state.canUndo)
        assertTrue(state.showStartMatchDialog)
    }

    @Test
    fun testIncrementScore1() {
        viewModel.incrementScore1()
        val state = viewModel.uiState.value
        assertEquals(1, state.score1)
        assertEquals(0, state.score2)
        assertTrue(state.canUndo)
    }

    @Test
    fun testIncrementScore2() {
        viewModel.incrementScore2()
        val state = viewModel.uiState.value
        assertEquals(0, state.score1)
        assertEquals(1, state.score2)
        assertTrue(state.canUndo)
    }

    @Test
    fun testUndo() {
        // Increment player 1
        viewModel.incrementScore1()
        assertTrue(viewModel.uiState.value.canUndo)

        // Increment player 2
        viewModel.incrementScore2()
        assertEquals(1, viewModel.uiState.value.score1)
        assertEquals(1, viewModel.uiState.value.score2)

        // Undo once
        viewModel.undo()
        var state = viewModel.uiState.value
        assertEquals(1, state.score1)
        assertEquals(0, state.score2)
        assertTrue(state.canUndo)

        // Undo twice
        viewModel.undo()
        state = viewModel.uiState.value
        assertEquals(0, state.score1)
        assertEquals(0, state.score2)
        assertFalse(state.canUndo)
    }

    @Test
    fun testReset() {
        viewModel.incrementScore1()
        viewModel.incrementScore1()
        viewModel.incrementScore2()

        viewModel.reset()

        val state = viewModel.uiState.value
        assertEquals(0, state.score1)
        assertEquals(0, state.score2)
        assertNull(state.status1)
        assertNull(state.status2)
        assertTrue(state.canUndo) // Reset is undoable!
    }

    @Test
    fun testMatchPointStatusTransitions() {
        // Play to 20-19
        repeat(20) { viewModel.incrementScore1() }
        repeat(19) { viewModel.incrementScore2() }

        assertEquals(MatchStatus.MATCH_POINT, viewModel.uiState.value.status1)
        assertNull(viewModel.uiState.value.status2)

        // Score 21-19 (Player 1 wins)
        viewModel.incrementScore1()
        assertEquals(MatchStatus.WINNER, viewModel.uiState.value.status1)
        assertNull(viewModel.uiState.value.status2)
    }

    @Test
    fun testNavigation() {
        assertEquals(AppScreen.SCOREBOARD, viewModel.uiState.value.currentScreen)
        viewModel.navigateTo(AppScreen.HISTORY)
        assertEquals(AppScreen.HISTORY, viewModel.uiState.value.currentScreen)
        viewModel.navigateTo(AppScreen.PLAYERS)
        assertEquals(AppScreen.PLAYERS, viewModel.uiState.value.currentScreen)
    }

    @Test
    fun testStartMatchAndReset() {
        viewModel.startMatch("Alice", "Bob")
        val state = viewModel.uiState.value
        assertEquals("Alice", state.name1)
        assertEquals("Bob", state.name2)
        assertEquals(0, state.score1)
        assertEquals(0, state.score2)
        assertFalse(state.matchSaved)
    }

    @Test
    fun testPlayerManagement() {
        viewModel.addPlayer(" Charlie ")
        val players = viewModel.uiState.value.players
        assertEquals(1, players.size)
        assertEquals("Charlie", players[0].name)

        viewModel.deletePlayer(players[0])
        assertEquals(0, viewModel.uiState.value.players.size)
    }

    @Test
    fun testMatchPersistenceOnWinner() = kotlinx.coroutines.runBlocking {
        playerRepository.addPlayer("John")
        playerRepository.addPlayer("Bert")

        viewModel.startMatch("John", "Bert")
        // Score to 20-0
        repeat(20) { viewModel.incrementScore1() }
        // No match should be persisted yet
        assertEquals(0, viewModel.uiState.value.matchHistory.size)
        assertFalse(viewModel.uiState.value.matchSaved)

        // Score 21 (Winner point)
        viewModel.incrementScore1()
        assertTrue(viewModel.uiState.value.matchSaved)

        val matches = viewModel.uiState.value.matchHistory
        assertEquals(1, matches.size)

        val players = viewModel.uiState.value.players
        val johnId = players.find { it.name == "John" }?.id
        val bertId = players.find { it.name == "Bert" }?.id

        assertEquals(johnId, matches[0].player1Id)
        assertEquals(bertId, matches[0].player2Id)
        assertEquals(21, matches[0].score1)
        assertEquals(0, matches[0].score2)
        assertEquals(johnId, matches[0].winnerId)
    }

    @Test
    fun testCustomWinningPoints() {
        // Change winning points to 11
        settingsRepository.setWinningPoints(11)
        viewModel.refreshSettings()

        // Play to 10-9
        repeat(10) { viewModel.incrementScore1() }
        repeat(9) { viewModel.incrementScore2() }

        assertEquals(MatchStatus.MATCH_POINT, viewModel.uiState.value.status1)
        assertNull(viewModel.uiState.value.status2)

        // Score 11-9 (Player 1 wins)
        viewModel.incrementScore1()
        assertEquals(MatchStatus.WINNER, viewModel.uiState.value.status1)
    }
}
