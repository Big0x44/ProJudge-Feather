package de.big0x44.projudgefeather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import de.big0x44.projudgefeather.model.SharedPreferencesSettingsRepository
import de.big0x44.projudgefeather.ui.scoreboard.ScoreboardScreen
import de.big0x44.projudgefeather.ui.scoreboard.ScoreboardViewModel
import de.big0x44.projudgefeather.ui.scoreboard.ScoreboardViewModelFactory
import de.big0x44.projudgefeather.ui.settings.SettingsScreen
import de.big0x44.projudgefeather.ui.settings.SettingsViewModel
import de.big0x44.projudgefeather.ui.settings.SettingsViewModelFactory
import de.big0x44.projudgefeather.ui.theme.ProJudgeFeatherTheme

enum class AppScreen {
    SCOREBOARD,
    SETTINGS
}

class MainActivity : ComponentActivity() {
    private val settingsRepository by lazy {
        SharedPreferencesSettingsRepository(applicationContext)
    }

    private val scoreboardViewModel: ScoreboardViewModel by viewModels {
        ScoreboardViewModelFactory(settingsRepository)
    }

    private val settingsViewModel: SettingsViewModel by viewModels {
        SettingsViewModelFactory(settingsRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProJudgeFeatherTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    var currentScreen by remember { mutableStateOf(AppScreen.SCOREBOARD) }

                    when (currentScreen) {
                        AppScreen.SCOREBOARD -> {
                            ScoreboardScreen(
                                viewModel = scoreboardViewModel,
                                onSettingsClick = { currentScreen = AppScreen.SETTINGS },
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                        AppScreen.SETTINGS -> {
                            SettingsScreen(
                                viewModel = settingsViewModel,
                                onBack = { currentScreen = AppScreen.SCOREBOARD },
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                    }
                }
            }
        }
    }
}