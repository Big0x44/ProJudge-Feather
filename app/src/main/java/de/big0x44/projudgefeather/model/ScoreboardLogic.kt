package de.big0x44.projudgefeather.model

object ScoreboardLogic {
    /**
     * Determines the status of a player based on official Badminton rules.
     * Returns "WINNER" if the player has won the game, "MATCH POINT" if the player
     * is at match point, and null otherwise.
     */
    fun getPlayerStatus(playerScore: Int, opponentScore: Int): String? {
        // Winner rules: First to 21 (must lead by 2) or first to 30
        if (playerScore >= 21 && playerScore - opponentScore >= 2) return "WINNER"
        if (playerScore == 30) return "WINNER"

        // Match point rules: Player has reached at least 20, is leading, and opponent has not already won
        if (playerScore >= 20 && playerScore > opponentScore) {
            val opponentHasWon = (opponentScore >= 21 && opponentScore - playerScore >= 2) || opponentScore == 30
            if (!opponentHasWon) return "MATCH POINT"
        }
        return null
    }
}
