package de.big0x44.projudgefeather

import de.big0x44.projudgefeather.model.MatchStatus
import de.big0x44.projudgefeather.model.ScoreboardLogic
import org.junit.Test
import org.junit.Assert.*

class ExampleUnitTest {

    @Test
    fun testNormalPlayStatus() {
        // Initial state
        assertNull(ScoreboardLogic.getPlayerStatus(0, 0))
        assertNull(ScoreboardLogic.getPlayerStatus(0, 0))

        // Mid-game
        assertNull(ScoreboardLogic.getPlayerStatus(11, 10))
        assertNull(ScoreboardLogic.getPlayerStatus(10, 11))
        assertNull(ScoreboardLogic.getPlayerStatus(19, 19))
    }

    @Test
    fun testMatchPointStatus() {
        // First player to 20 leading by 1 or more (before winning)
        assertEquals(MatchStatus.MATCH_POINT, ScoreboardLogic.getPlayerStatus(20, 19))
        assertNull(ScoreboardLogic.getPlayerStatus(19, 20))

        // In a deuce situation, the leading player gets match point
        assertEquals(MatchStatus.MATCH_POINT, ScoreboardLogic.getPlayerStatus(21, 20))
        assertNull(ScoreboardLogic.getPlayerStatus(20, 21))

        // Leading at 29-28 (next point 30 wins)
        assertEquals(MatchStatus.MATCH_POINT, ScoreboardLogic.getPlayerStatus(29, 28))
    }

    @Test
    fun testWinnerStatus() {
        // Normal win at 21 (lead by 2)
        assertEquals(MatchStatus.WINNER, ScoreboardLogic.getPlayerStatus(21, 19))
        assertNull(ScoreboardLogic.getPlayerStatus(19, 21))

        // Deuce win (must lead by 2)
        assertEquals(MatchStatus.WINNER, ScoreboardLogic.getPlayerStatus(22, 20))
        assertNull(ScoreboardLogic.getPlayerStatus(20, 22))

        // Cap win at 30 points (even if lead is only 1 point: 30-29)
        assertEquals(MatchStatus.WINNER, ScoreboardLogic.getPlayerStatus(30, 29))
        assertNull(ScoreboardLogic.getPlayerStatus(29, 30))
    }

    @Test
    fun testMatchWinnerIdConstraint() {
        // Validation passes when winnerId is either player1Id or player2Id
        val validResult1 = de.big0x44.projudgefeather.model.MatchResult(
            player1Id = "p1",
            player2Id = "p2",
            score1 = 21,
            score2 = 19,
            timestamp = 1000L,
            winnerId = "p1"
        )
        assertEquals("p1", validResult1.winnerId)

        val validResult2 = de.big0x44.projudgefeather.model.MatchResult(
            player1Id = "p1",
            player2Id = "p2",
            score1 = 19,
            score2 = 21,
            timestamp = 1000L,
            winnerId = "p2"
        )
        assertEquals("p2", validResult2.winnerId)

        // Validation fails when winnerId is a third ID
        try {
            de.big0x44.projudgefeather.model.MatchResult(
                player1Id = "p1",
                player2Id = "p2",
                score1 = 21,
                score2 = 19,
                timestamp = 1000L,
                winnerId = "p3"
            )
            fail("Expected IllegalArgumentException because winnerId does not match player1Id or player2Id")
        } catch (e: IllegalArgumentException) {
            assertTrue(e.message?.contains("Winner ID (p3) must be either Player 1 ID (p1) or Player 2 ID (p2)") == true)
        }
    }
}