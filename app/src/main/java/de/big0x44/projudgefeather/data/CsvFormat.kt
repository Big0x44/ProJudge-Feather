package de.big0x44.projudgefeather.data

object CsvFormat {
    const val DELIMITER = ','
    const val HEADER = "id,player1,player2,score1,score2,timestamp,winner"
    
    const val COL_ID = 0
    const val COL_PLAYER1 = 1
    const val COL_PLAYER2 = 2
    const val COL_SCORE1 = 3
    const val COL_SCORE2 = 4
    const val COL_TIMESTAMP = 5
    const val COL_WINNER = 6
    
    const val EXPECTED_COLUMNS = 7

    fun escape(value: String): String {
        val needQuotes = value.contains(DELIMITER) || value.contains("\"") || value.contains("\n") || value.contains("\r")
        val escaped = value.replace("\"", "\"\"")
        return if (needQuotes) "\"$escaped\"" else escaped
    }
}
