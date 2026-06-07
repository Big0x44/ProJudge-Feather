package de.big0x44.projudgefeather.model

import kotlinx.coroutines.flow.Flow

interface PlayerRepository {
    fun getAllPlayers(): Flow<List<Player>>
    suspend fun addPlayer(name: String): Boolean
    suspend fun addPlayer(id: String, name: String): Boolean
    suspend fun deletePlayer(player: Player)
}

interface MatchResultRepository {
    fun getAllMatches(): Flow<List<MatchResult>>
    suspend fun saveMatch(match: MatchResult)
    suspend fun saveAll(matches: List<MatchResult>)
    suspend fun clearAllMatches()
}
