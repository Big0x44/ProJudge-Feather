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
                    player1Name = it.player1Name,
                    player2Name = it.player2Name,
                    score1 = it.score1,
                    score2 = it.score2,
                    timestamp = it.timestamp,
                    winnerName = it.winnerName
                )
            }
        }
    }

    override suspend fun saveMatch(match: MatchResult) {
        matchResultDao.insertMatch(
            MatchResultEntity(
                player1Name = match.player1Name,
                player2Name = match.player2Name,
                score1 = match.score1,
                score2 = match.score2,
                timestamp = match.timestamp,
                winnerName = match.winnerName
            )
        )
    }

    override suspend fun saveAll(matches: List<MatchResult>) {
        val entities = matches.map {
            MatchResultEntity(
                player1Name = it.player1Name,
                player2Name = it.player2Name,
                score1 = it.score1,
                score2 = it.score2,
                timestamp = it.timestamp,
                winnerName = it.winnerName
            )
        }
        matchResultDao.insertAll(entities)
    }

    override suspend fun clearAllMatches() {
        matchResultDao.deleteAllMatches()
    }
}
