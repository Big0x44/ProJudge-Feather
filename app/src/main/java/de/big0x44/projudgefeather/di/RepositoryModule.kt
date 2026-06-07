package de.big0x44.projudgefeather.di

import de.big0x44.projudgefeather.data.repository.MatchResultRepositoryImpl
import de.big0x44.projudgefeather.data.repository.PlayerRepositoryImpl
import de.big0x44.projudgefeather.model.MatchResultRepository
import de.big0x44.projudgefeather.model.PlayerRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindPlayerRepository(
        playerRepositoryImpl: PlayerRepositoryImpl
    ): PlayerRepository

    @Binds
    @Singleton
    abstract fun bindMatchResultRepository(
        matchResultRepositoryImpl: MatchResultRepositoryImpl
    ): MatchResultRepository
}
