package de.big0x44.projudgefeather.model

data class MatchResult(
    val id: Int = 0,
    val player1Name: String,
    val player2Name: String,
    val score1: Int,
    val score2: Int,
    val timestamp: Long,
    val winnerName: String
)
