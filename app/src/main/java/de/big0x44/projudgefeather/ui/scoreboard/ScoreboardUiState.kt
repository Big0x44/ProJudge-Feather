package de.big0x44.projudgefeather.ui.scoreboard

import de.big0x44.projudgefeather.model.MatchStatus

data class ScoreboardUiState(
    val score1: Int = 0,
    val score2: Int = 0,
    val name1: String? = null,
    val name2: String? = null,
    val status1: MatchStatus? = null,
    val status2: MatchStatus? = null,
    val canUndo: Boolean = false
)
