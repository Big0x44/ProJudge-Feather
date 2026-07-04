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
import de.big0x44.projudgefeather.ui.scoreboard.ScoreboardScreen
import de.big0x44.projudgefeather.ui.scoreboard.ScoreboardViewModel
import de.big0x44.projudgefeather.ui.settings.SettingsScreen
import de.big0x44.projudgefeather.ui.settings.SettingsViewModel
import de.big0x44.projudgefeather.ui.theme.ProJudgeFeatherTheme
import dagger.hilt.android.AndroidEntryPoint

enum class MainScreen {
    SCOREBOARD,
    SETTINGS
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val scoreboardViewModel: ScoreboardViewModel by viewModels()
    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProJudgeFeatherTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    var currentScreen by remember { mutableStateOf(MainScreen.SCOREBOARD) }

                    when (currentScreen) {
                        MainScreen.SCOREBOARD -> {
                            ScoreboardScreen(
                                viewModel = scoreboardViewModel,
                                onSettingsClick = { currentScreen = MainScreen.SETTINGS },
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                        MainScreen.SETTINGS -> {
                            SettingsScreen(
                                viewModel = settingsViewModel,
                                onBack = { currentScreen = MainScreen.SCOREBOARD },
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                    }
                }
            }
        }
    }
}