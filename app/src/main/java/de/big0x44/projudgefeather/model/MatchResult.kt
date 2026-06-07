package de.big0x44.projudgefeather.model

data class MatchResult(
    val id: String = java.util.UUID.randomUUID().toString(),
    val player1Id: String,
    val player2Id: String,
    val score1: Int,
    val score2: Int,
    val timestamp: Long,
    val winnerId: String
) {
    init {
        require(winnerId == player1Id || winnerId == player2Id) {
            "Winner ID ($winnerId) must be either Player 1 ID ($player1Id) or Player 2 ID ($player2Id)"
        }
    }
}
