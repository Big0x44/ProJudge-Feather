package de.big0x44.projudgefeather.ui.scoreboard

import de.big0x44.projudgefeather.model.MatchStatus
import de.big0x44.projudgefeather.model.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ScoreboardViewModelTest {

    private lateinit var viewModel: ScoreboardViewModel
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
        settingsRepository = FakeSettingsRepository()
        viewModel = ScoreboardViewModel(settingsRepository)
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
    fun testRenamePlayer1() {
        viewModel.renamePlayer1("Alice")
        assertEquals("Alice", viewModel.uiState.value.name1)

        // Blank name should clear custom name and revert to null (fallback to localized default)
        viewModel.renamePlayer1("  ")
        assertNull(viewModel.uiState.value.name1)
    }

    @Test
    fun testRenamePlayer2() {
        viewModel.renamePlayer2("Bob")
        assertEquals("Bob", viewModel.uiState.value.name2)

        // Empty name should clear custom name and revert to null (fallback to localized default)
        viewModel.renamePlayer2("")
        assertNull(viewModel.uiState.value.name2)
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
