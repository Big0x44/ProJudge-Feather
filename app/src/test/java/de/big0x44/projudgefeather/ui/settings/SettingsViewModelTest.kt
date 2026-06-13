package de.big0x44.projudgefeather.ui.settings

import de.big0x44.projudgefeather.model.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SettingsViewModelTest {

    private lateinit var viewModel: SettingsViewModel
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
        viewModel = SettingsViewModel(settingsRepository)
    }

    @Test
    fun testInitialState() {
        assertEquals(21, viewModel.uiState.value.winningPoints)
    }

    @Test
    fun testUpdateWinningPoints() {
        viewModel.updateWinningPoints(15)
        assertEquals(15, viewModel.uiState.value.winningPoints)
        assertEquals(15, settingsRepository.winningPoints.value)
    }

    @Test
    fun testUpdateWinningPointsLimits() {
        viewModel.updateWinningPoints(2) // Should coerce to 5
        assertEquals(5, viewModel.uiState.value.winningPoints)

        viewModel.updateWinningPoints(60) // Should coerce to 50
        assertEquals(50, viewModel.uiState.value.winningPoints)
    }
}
