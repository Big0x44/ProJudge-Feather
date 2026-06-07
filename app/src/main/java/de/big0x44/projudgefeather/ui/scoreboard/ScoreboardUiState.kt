package de.big0x44.projudgefeather.ui.scoreboard

import de.big0x44.projudgefeather.model.MatchStatus
import de.big0x44.projudgefeather.model.Player
import de.big0x44.projudgefeather.model.MatchResult

enum class AppScreen {
    SCOREBOARD,
    HISTORY,
    PLAYERS
}

data class ScoreboardUiState(
    val currentScreen: AppScreen = AppScreen.SCOREBOARD,
    val score1: Int = 0,
    val score2: Int = 0,
    val name1: String? = null,
    val name2: String? = null,
    val status1: MatchStatus? = null,
    val status2: MatchStatus? = null,
    val canUndo: Boolean = false,
    val matchSaved: Boolean = false,
    val players: List<Player> = emptyList(),
    val matchHistory: List<MatchResult> = emptyList(),
    val showStartMatchDialog: Boolean = true
)
