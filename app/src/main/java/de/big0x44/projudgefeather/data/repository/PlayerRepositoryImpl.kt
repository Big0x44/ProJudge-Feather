package de.big0x44.projudgefeather.data.repository

import de.big0x44.projudgefeather.data.database.PlayerDao
import de.big0x44.projudgefeather.data.database.PlayerEntity
import de.big0x44.projudgefeather.model.Player
import de.big0x44.projudgefeather.model.PlayerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PlayerRepositoryImpl @Inject constructor(private val playerDao: PlayerDao) : PlayerRepository {
    override fun getAllPlayers(): Flow<List<Player>> {
        return playerDao.getAllPlayers().map { entities ->
            entities.map { Player(id = it.id, name = it.name) }
        }
    }

    override suspend fun addPlayer(name: String): Boolean {
        val trimmed = name.trim()
        if (trimmed.isEmpty()) return false
        val rowId = playerDao.insertPlayer(PlayerEntity(name = trimmed))
        return rowId != -1L
    }

    override suspend fun deletePlayer(player: Player) {
        playerDao.deletePlayer(PlayerEntity(id = player.id, name = player.name))
    }
}
