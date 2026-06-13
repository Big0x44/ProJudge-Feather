package de.big0x44.projudgefeather.model

enum class MatchStatus {
    MATCH_POINT,
    WINNER
}

object ScoreboardLogic {
    /**
     * Determines the status of a player based on official Badminton rules.
     * Returns MatchStatus.WINNER if the player has won the game, MatchStatus.MATCH_POINT
     * if the player is at match point, and null otherwise.
     */
    fun getPlayerStatus(playerScore: Int, opponentScore: Int, targetPoints: Int = 21): MatchStatus? {
        val cap = targetPoints + 9
        // Winner rules: First to targetPoints (must lead by 2) or first to cap
        if (playerScore >= targetPoints && playerScore - opponentScore >= 2) return MatchStatus.WINNER
        if (playerScore == cap) return MatchStatus.WINNER

        // Match point rules: Player has reached at least (targetPoints - 1), is leading, and opponent has not already won
        val matchPointThreshold = targetPoints - 1
        if (playerScore >= matchPointThreshold && playerScore > opponentScore) {
            val opponentHasWon = (opponentScore >= targetPoints && opponentScore - playerScore >= 2) || opponentScore == cap
            if (!opponentHasWon) return MatchStatus.MATCH_POINT
        }
        return null
    }
}
