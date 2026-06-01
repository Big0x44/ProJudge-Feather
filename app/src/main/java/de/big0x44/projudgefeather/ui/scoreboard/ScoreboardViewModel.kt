package de.big0x44.projudgefeather.ui.scoreboard

import androidx.lifecycle.ViewModel
import de.big0x44.projudgefeather.model.ScoreboardLogic
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ScoreboardViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ScoreboardUiState())
    val uiState: StateFlow<ScoreboardUiState> = _uiState.asStateFlow()

    private val history = mutableListOf<Pair<Int, Int>>()

    fun incrementScore1() {
        saveToHistory()
        _uiState.update { currentState ->
            val newScore = currentState.score1 + 1
            currentState.copy(
                score1 = newScore,
                status1 = ScoreboardLogic.getPlayerStatus(newScore, currentState.score2),
                status2 = ScoreboardLogic.getPlayerStatus(currentState.score2, newScore),
                canUndo = true
            )
        }
    }

    fun incrementScore2() {
        saveToHistory()
        _uiState.update { currentState ->
            val newScore = currentState.score2 + 1
            currentState.copy(
                score2 = newScore,
                status1 = ScoreboardLogic.getPlayerStatus(currentState.score1, newScore),
                status2 = ScoreboardLogic.getPlayerStatus(newScore, currentState.score1),
                canUndo = true
            )
        }
    }

    fun undo() {
        if (history.isNotEmpty()) {
            val lastState = history.removeAt(history.size - 1)
            _uiState.update { currentState ->
                currentState.copy(
                    score1 = lastState.first,
                    score2 = lastState.second,
                    status1 = ScoreboardLogic.getPlayerStatus(lastState.first, lastState.second),
                    status2 = ScoreboardLogic.getPlayerStatus(lastState.second, lastState.first),
                    canUndo = history.isNotEmpty()
                )
            }
        }
    }

    fun reset() {
        saveToHistory()
        _uiState.update { currentState ->
            currentState.copy(
                score1 = 0,
                score2 = 0,
                status1 = null,
                status2 = null,
                canUndo = true
            )
        }
    }

    fun renamePlayer1(newName: String) {
        val name = newName.trim()
        _uiState.update { currentState ->
            currentState.copy(name1 = if (name.isNotEmpty()) name else null)
        }
    }

    fun renamePlayer2(newName: String) {
        val name = newName.trim()
        _uiState.update { currentState ->
            currentState.copy(name2 = if (name.isNotEmpty()) name else null)
        }
    }

    private fun saveToHistory() {
        if (history.size >= 50) {
            history.removeAt(0)
        }
        val current = _uiState.value
        history.add(current.score1 to current.score2)
    }
}
