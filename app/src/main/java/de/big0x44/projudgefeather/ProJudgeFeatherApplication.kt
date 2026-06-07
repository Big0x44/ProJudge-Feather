package de.big0x44.projudgefeather

import android.app.Application
import de.big0x44.projudgefeather.data.database.AppDatabase
import de.big0x44.projudgefeather.data.repository.MatchResultRepositoryImpl
import de.big0x44.projudgefeather.data.repository.PlayerRepositoryImpl
import de.big0x44.projudgefeather.model.MatchResultRepository
import de.big0x44.projudgefeather.model.PlayerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class ProJudgeFeatherApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy { AppDatabase.getDatabase(this, applicationScope) }
    val playerRepository: PlayerRepository by lazy { PlayerRepositoryImpl(database.playerDao()) }
    val matchResultRepository: MatchResultRepository by lazy { MatchResultRepositoryImpl(database.matchResultDao()) }
}
