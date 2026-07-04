package de.big0x44.projudgefeather.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.big0x44.projudgefeather.model.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val settingsRepository: SettingsRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(
        SettingsUiState(winningPoints = settingsRepository.winningPoints.value)
    )
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    fun updateWinningPoints(points: Int) {
        val sanitizedPoints = points.coerceIn(5, 50)
        settingsRepository.setWinningPoints(sanitizedPoints)
        _uiState.update { it.copy(winningPoints = sanitizedPoints) }
    }
}

class SettingsViewModelFactory(private val settingsRepository: SettingsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SettingsViewModel(settingsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
