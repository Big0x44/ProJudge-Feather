package de.big0x44.projudgefeather

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
        assertEquals("MATCH POINT", ScoreboardLogic.getPlayerStatus(20, 19))
        assertNull(ScoreboardLogic.getPlayerStatus(19, 20))

        // In a deuce situation, the leading player gets match point
        assertEquals("MATCH POINT", ScoreboardLogic.getPlayerStatus(21, 20))
        assertNull(ScoreboardLogic.getPlayerStatus(20, 21))

        // Leading at 29-28 (next point 30 wins)
        assertEquals("MATCH POINT", ScoreboardLogic.getPlayerStatus(29, 28))
    }

    @Test
    fun testWinnerStatus() {
        // Normal win at 21 (lead by 2)
        assertEquals("WINNER", ScoreboardLogic.getPlayerStatus(21, 19))
        assertNull(ScoreboardLogic.getPlayerStatus(19, 21))

        // Deuce win (must lead by 2)
        assertEquals("WINNER", ScoreboardLogic.getPlayerStatus(22, 20))
        assertNull(ScoreboardLogic.getPlayerStatus(20, 22))

        // Cap win at 30 points (even if lead is only 1 point: 30-29)
        assertEquals("WINNER", ScoreboardLogic.getPlayerStatus(30, 29))
        assertNull(ScoreboardLogic.getPlayerStatus(29, 30))
    }
}