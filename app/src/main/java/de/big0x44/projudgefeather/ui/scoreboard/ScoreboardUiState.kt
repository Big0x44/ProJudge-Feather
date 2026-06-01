package de.big0x44.projudgefeather.ui.scoreboard

data class ScoreboardUiState(
    val score1: Int = 0,
    val score2: Int = 0,
    val name1: String = "Player 1",
    val name2: String = "Player 2",
    val status1: String? = null,
    val status2: String? = null,
    val canUndo: Boolean = false
)
