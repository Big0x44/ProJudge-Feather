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
    fun getPlayerStatus(playerScore: Int, opponentScore: Int): MatchStatus? {
        // Winner rules: First to 21 (must lead by 2) or first to 30
        if (playerScore >= 21 && playerScore - opponentScore >= 2) return MatchStatus.WINNER
        if (playerScore == 30) return MatchStatus.WINNER

        // Match point rules: Player has reached at least 20, is leading, and opponent has not already won
        if (playerScore >= 20 && playerScore > opponentScore) {
            val opponentHasWon = (opponentScore >= 21 && opponentScore - playerScore >= 2) || opponentScore == 30
            if (!opponentHasWon) return MatchStatus.MATCH_POINT
        }
        return null
    }
}
