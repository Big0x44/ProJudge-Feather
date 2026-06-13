package de.big0x44.projudgefeather.model

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.core.content.edit

interface SettingsRepository {
    val winningPoints: StateFlow<Int>
    fun setWinningPoints(points: Int)
}

class SharedPreferencesSettingsRepository(context: Context) : SettingsRepository {
    private val prefs: SharedPreferences = context.getSharedPreferences("projudge_prefs", Context.MODE_PRIVATE)

    private val _winningPoints = MutableStateFlow(
        prefs.getInt(KEY_WINNING_POINTS, DEFAULT_WINNING_POINTS)
    )
    override val winningPoints: StateFlow<Int> = _winningPoints.asStateFlow()

    companion object {
        private const val KEY_WINNING_POINTS = "winning_points"
        private const val DEFAULT_WINNING_POINTS = 21
    }

    override fun setWinningPoints(points: Int) {
        prefs.edit { putInt(KEY_WINNING_POINTS, points) }
        _winningPoints.value = points
    }
}
