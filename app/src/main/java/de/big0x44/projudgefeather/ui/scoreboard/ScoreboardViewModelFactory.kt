package de.big0x44.projudgefeather.ui.scoreboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.big0x44.projudgefeather.model.MatchResultRepository
import de.big0x44.projudgefeather.model.PlayerRepository

class ScoreboardViewModelFactory(
    private val playerRepository: PlayerRepository,
    private val matchResultRepository: MatchResultRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScoreboardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ScoreboardViewModel(playerRepository, matchResultRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
