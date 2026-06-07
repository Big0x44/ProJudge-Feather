package de.big0x44.projudgefeather.data.repository

import de.big0x44.projudgefeather.data.database.MatchResultDao
import de.big0x44.projudgefeather.data.database.MatchResultEntity
import de.big0x44.projudgefeather.model.MatchResult
import de.big0x44.projudgefeather.model.MatchResultRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MatchResultRepositoryImpl @Inject constructor(private val matchResultDao: MatchResultDao) : MatchResultRepository {
    override fun getAllMatches(): Flow<List<MatchResult>> {
        return matchResultDao.getAllMatches().map { entities ->
            entities.map {
                MatchResult(
                    id = it.id,
                    player1Id = it.player1Id,
                    player2Id = it.player2Id,
                    score1 = it.score1,
                    score2 = it.score2,
                    timestamp = it.timestamp,
                    winnerId = it.winnerId
                )
            }
        }
    }

    override suspend fun saveMatch(match: MatchResult) {
        matchResultDao.insertMatch(
            MatchResultEntity(
                id = match.id,
                player1Id = match.player1Id,
                player2Id = match.player2Id,
                score1 = match.score1,
                score2 = match.score2,
                timestamp = match.timestamp,
                winnerId = match.winnerId
            )
        )
    }

    override suspend fun saveAll(matches: List<MatchResult>) {
        val entities = matches.map {
            MatchResultEntity(
                id = it.id,
                player1Id = it.player1Id,
                player2Id = it.player2Id,
                score1 = it.score1,
                score2 = it.score2,
                timestamp = it.timestamp,
                winnerId = it.winnerId
            )
        }
        matchResultDao.insertAll(entities)
    }

    override suspend fun clearAllMatches() {
        matchResultDao.deleteAllMatches()
    }
}
