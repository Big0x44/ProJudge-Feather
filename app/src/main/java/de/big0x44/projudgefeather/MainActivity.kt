package de.big0x44.projudgefeather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import de.big0x44.projudgefeather.ui.scoreboard.ScoreboardScreen
import de.big0x44.projudgefeather.ui.scoreboard.ScoreboardViewModel
import de.big0x44.projudgefeather.ui.scoreboard.ScoreboardViewModelFactory
import de.big0x44.projudgefeather.ui.theme.ProJudgeFeatherTheme

class MainActivity : ComponentActivity() {
    private val viewModel: ScoreboardViewModel by viewModels {
        ScoreboardViewModelFactory(
            (application as ProJudgeFeatherApplication).playerRepository,
            (application as ProJudgeFeatherApplication).matchResultRepository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProJudgeFeatherTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ScoreboardScreen(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}