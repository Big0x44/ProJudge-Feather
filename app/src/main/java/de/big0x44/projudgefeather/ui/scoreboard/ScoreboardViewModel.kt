package de.big0x44.projudgefeather.ui.scoreboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.big0x44.projudgefeather.data.CsvHelper
import de.big0x44.projudgefeather.model.MatchResult
import de.big0x44.projudgefeather.model.MatchResultRepository
import de.big0x44.projudgefeather.model.MatchStatus
import de.big0x44.projudgefeather.model.Player
import de.big0x44.projudgefeather.model.PlayerRepository
import de.big0x44.projudgefeather.model.ScoreboardLogic
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.OutputStream

class ScoreboardViewModel(
    private val playerRepository: PlayerRepository,
    private val matchResultRepository: MatchResultRepository,
    private val externalScope: CoroutineScope? = null,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val currentScope = externalScope ?: viewModelScope
    private val _uiState = MutableStateFlow(ScoreboardUiState())
    val uiState: StateFlow<ScoreboardUiState> = _uiState.asStateFlow()

    private val history = mutableListOf<Pair<Int, Int>>()

    init {
        // Collect defined players from database
        currentScope.launch {
            playerRepository.getAllPlayers().collect { list ->
                _uiState.update { it.copy(players = list) }
            }
        }
        // Collect match history from database
        currentScope.launch {
            matchResultRepository.getAllMatches().collect { list ->
                _uiState.update { it.copy(matchHistory = list) }
            }
        }
    }

    fun navigateTo(screen: AppScreen) {
        _uiState.update { it.copy(currentScreen = screen) }
    }

    fun showStartMatchDialog(show: Boolean) {
        _uiState.update { it.copy(showStartMatchDialog = show) }
    }

    fun startMatch(player1Name: String, player2Name: String) {
        val p1 = player1Name.trim()
        val p2 = player2Name.trim()
        history.clear()
        _uiState.update { currentState ->
            currentState.copy(
                score1 = 0,
                score2 = 0,
                name1 = if (p1.isNotEmpty()) p1 else null,
                name2 = if (p2.isNotEmpty()) p2 else null,
                status1 = null,
                status2 = null,
                canUndo = false,
                matchSaved = false,
                showStartMatchDialog = false
            )
        }
    }

    fun incrementScore1() {
        if (_uiState.value.status1 == MatchStatus.WINNER || _uiState.value.status2 == MatchStatus.WINNER) return

        saveToHistory()
        var shouldSave = false
        var p1Score = 0
        var p2Score = 0
        var p1Name = ""
        var p2Name = ""
        var winnerName = ""

        _uiState.update { currentState ->
            val newScore = currentState.score1 + 1
            val status1 = ScoreboardLogic.getPlayerStatus(newScore, currentState.score2)
            val status2 = ScoreboardLogic.getPlayerStatus(currentState.score2, newScore)

            p1Score = newScore
            p2Score = currentState.score2
            p1Name = currentState.name1 ?: "Player 1"
            p2Name = currentState.name2 ?: "Player 2"

            val isWinner1 = status1 == MatchStatus.WINNER
            val isWinner2 = status2 == MatchStatus.WINNER

            if ((isWinner1 || isWinner2) && !currentState.matchSaved) {
                shouldSave = true
                winnerName = if (isWinner1) p1Name else p2Name
            }

            currentState.copy(
                score1 = newScore,
                status1 = status1,
                status2 = status2,
                canUndo = true,
                matchSaved = currentState.matchSaved || isWinner1 || isWinner2
            )
        }

        if (shouldSave) {
            saveFinishedMatch(p1Name, p2Name, p1Score, p2Score, winnerName)
        }
    }

    fun incrementScore2() {
        if (_uiState.value.status1 == MatchStatus.WINNER || _uiState.value.status2 == MatchStatus.WINNER) return

        saveToHistory()
        var shouldSave = false
        var p1Score = 0
        var p2Score = 0
        var p1Name = ""
        var p2Name = ""
        var winnerName = ""

        _uiState.update { currentState ->
            val newScore = currentState.score2 + 1
            val status1 = ScoreboardLogic.getPlayerStatus(currentState.score1, newScore)
            val status2 = ScoreboardLogic.getPlayerStatus(newScore, currentState.score1)

            p1Score = currentState.score1
            p2Score = newScore
            p1Name = currentState.name1 ?: "Player 1"
            p2Name = currentState.name2 ?: "Player 2"

            val isWinner1 = status1 == MatchStatus.WINNER
            val isWinner2 = status2 == MatchStatus.WINNER

            if ((isWinner1 || isWinner2) && !currentState.matchSaved) {
                shouldSave = true
                winnerName = if (isWinner1) p1Name else p2Name
            }

            currentState.copy(
                score2 = newScore,
                status1 = status1,
                status2 = status2,
                canUndo = true,
                matchSaved = currentState.matchSaved || isWinner1 || isWinner2
            )
        }

        if (shouldSave) {
            saveFinishedMatch(p1Name, p2Name, p1Score, p2Score, winnerName)
        }
    }

    private fun saveFinishedMatch(p1Name: String, p2Name: String, s1: Int, s2: Int, winner: String) {
        currentScope.launch(ioDispatcher) {
            val result = MatchResult(
                player1Name = p1Name,
                player2Name = p2Name,
                score1 = s1,
                score2 = s2,
                timestamp = System.currentTimeMillis(),
                winnerName = winner
            )
            matchResultRepository.saveMatch(result)
        }
    }

    fun undo() {
        if (history.isNotEmpty()) {
            val lastState = history.removeAt(history.size - 1)
            _uiState.update { currentState ->
                val status1 = ScoreboardLogic.getPlayerStatus(lastState.first, lastState.second)
                val status2 = ScoreboardLogic.getPlayerStatus(lastState.second, lastState.first)
                val isWinner = status1 == MatchStatus.WINNER || status2 == MatchStatus.WINNER

                currentState.copy(
                    score1 = lastState.first,
                    score2 = lastState.second,
                    status1 = status1,
                    status2 = status2,
                    canUndo = history.isNotEmpty(),
                    matchSaved = isWinner
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
                canUndo = true,
                matchSaved = false
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

    // Player Management
    fun addPlayer(name: String) {
        val trimmed = name.trim()
        if (trimmed.isEmpty()) return
        currentScope.launch(ioDispatcher) {
            playerRepository.addPlayer(trimmed)
        }
    }

    fun deletePlayer(player: Player) {
        currentScope.launch(ioDispatcher) {
            playerRepository.deletePlayer(player)
        }
    }

    // CSV Import / Export
    suspend fun exportHistory(outputStream: OutputStream) {
        withContext(ioDispatcher) {
            try {
                CsvHelper.exportToCsv(_uiState.value.matchHistory, outputStream)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun importHistory(inputStream: InputStream) {
        withContext(ioDispatcher) {
            try {
                val imported = CsvHelper.importFromCsv(inputStream)
                if (imported.isNotEmpty()) {
                    matchResultRepository.saveAll(imported)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun clearMatchHistory() {
        currentScope.launch(ioDispatcher) {
            matchResultRepository.clearAllMatches()
        }
    }
}
