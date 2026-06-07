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
    fun testCsvHelperExportAndImport() {
        val matches = listOf(
            de.big0x44.projudgefeather.model.MatchResult(
                player1Name = "John \"The Pro\" Doe",
                player2Name = "Alice, Bob & Co.",
                score1 = 21,
                score2 = 19,
                timestamp = 1780000000000L,
                winnerName = "John \"The Pro\" Doe"
            )
        )
        val baos = java.io.ByteArrayOutputStream()
        de.big0x44.projudgefeather.data.CsvHelper.exportToCsv(matches, baos)
        val csvContent = baos.toString("UTF-8")

        // Assert header is present and fields are escaped properly
        assertTrue(csvContent.contains("player1,player2,score1,score2,timestamp,winner"))
        assertTrue(csvContent.contains("\"John \"\"The Pro\"\" Doe\""))
        assertTrue(csvContent.contains("\"Alice, Bob & Co.\""))

        val bais = java.io.ByteArrayInputStream(baos.toByteArray())
        val importedMatches = de.big0x44.projudgefeather.data.CsvHelper.importFromCsv(bais)
        assertEquals(1, importedMatches.size)
        val imported = importedMatches[0]
        assertEquals("John \"The Pro\" Doe", imported.player1Name)
        assertEquals("Alice, Bob & Co.", imported.player2Name)
        assertEquals(21, imported.score1)
        assertEquals(19, imported.score2)
        assertEquals(1780000000000L, imported.timestamp)
        assertEquals("John \"The Pro\" Doe", imported.winnerName)
    }
}