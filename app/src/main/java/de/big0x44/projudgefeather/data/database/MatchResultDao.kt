package de.big0x44.projudgefeather.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MatchResultDao {
    @Query("SELECT * FROM match_results ORDER BY timestamp DESC")
    fun getAllMatches(): Flow<List<MatchResultEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatch(match: MatchResultEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(matches: List<MatchResultEntity>)

    @Query("DELETE FROM match_results")
    suspend fun deleteAllMatches()
}
