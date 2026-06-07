package de.big0x44.projudgefeather.data

object CsvFormat {
    const val DELIMITER = ','
    const val HEADER = "player1,player2,score1,score2,timestamp,winner"
    
    const val COL_PLAYER1 = 0
    const val COL_PLAYER2 = 1
    const val COL_SCORE1 = 2
    const val COL_SCORE2 = 3
    const val COL_TIMESTAMP = 4
    const val COL_WINNER = 5
    
    const val EXPECTED_COLUMNS = 6

    fun escape(value: String): String {
        val needQuotes = value.contains(DELIMITER) || value.contains("\"") || value.contains("\n") || value.contains("\r")
        val escaped = value.replace("\"", "\"\"")
        return if (needQuotes) "\"$escaped\"" else escaped
    }
}
