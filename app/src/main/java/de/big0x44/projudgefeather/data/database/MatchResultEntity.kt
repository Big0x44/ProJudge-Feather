package de.big0x44.projudgefeather.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "match_results")
data class MatchResultEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val player1Name: String,
    val player2Name: String,
    val score1: Int,
    val score2: Int,
    val timestamp: Long,
    val winnerName: String
)
