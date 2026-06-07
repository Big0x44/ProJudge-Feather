package de.big0x44.projudgefeather.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

data class MatchWithPlayerNames(
    val id: String,
    val player1Id: String,
    val player2Id: String,
    val score1: Int,
    val score2: Int,
    val timestamp: Long,
    val winnerId: String,
    val player1Name: String?,
    val player2Name: String?,
    val winnerName: String?
)

@Dao
interface MatchResultDao {
    @Query("""
        SELECT 
            m.id as id,
            m.player1Id as player1Id,
            m.player2Id as player2Id,
            m.score1 as score1,
            m.score2 as score2,
            m.timestamp as timestamp,
            m.winnerId as winnerId,
            p1.name as player1Name,
            p2.name as player2Name,
            pw.name as winnerName
        FROM match_results m
        LEFT JOIN players p1 ON m.player1Id = p1.id
        LEFT JOIN players p2 ON m.player2Id = p2.id
        LEFT JOIN players pw ON m.winnerId = pw.id
        ORDER BY m.timestamp DESC
    """)
    fun getAllMatches(): Flow<List<MatchWithPlayerNames>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatch(match: MatchResultEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(matches: List<MatchResultEntity>)

    @Query("DELETE FROM match_results")
    suspend fun deleteAllMatches()
}
