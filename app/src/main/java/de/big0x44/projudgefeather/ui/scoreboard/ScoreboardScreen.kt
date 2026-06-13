package de.big0x44.projudgefeather.ui.scoreboard

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import de.big0x44.projudgefeather.R
import de.big0x44.projudgefeather.model.MatchStatus
import de.big0x44.projudgefeather.ui.scoreboard.components.ControlPanel
import de.big0x44.projudgefeather.ui.scoreboard.components.PlayerHalf
import de.big0x44.projudgefeather.ui.scoreboard.components.RenameDialog
import de.big0x44.projudgefeather.ui.theme.ProJudgeFeatherTheme

@Composable
fun ScoreboardScreen(
    viewModel: ScoreboardViewModel,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    aaaa
    val uiState by viewModel.uiState.collectAsState()
    val view = LocalView.current

    DisposableEffect(view) {
        view.keepScreenOn = true
        onDispose {
            view.keepScreenOn = false
        }
    }

    LaunchedEffect(Unit) {
        viewModel.refreshSettings()
    }

    ScoreboardScreenContent(
        uiState = uiState,
        onIncrement1 = { viewModel.incrementScore1() },
        onIncrement2 = { viewModel.incrementScore2() },
        onUndo = { viewModel.undo() },
        onReset = { viewModel.reset() },
        onRename1 = { viewModel.renamePlayer1(it) },
        onRename2 = { viewModel.renamePlayer2(it) },
        onSettingsClick = onSettingsClick,
        modifier = modifier
    )
}

@Composable
fun ScoreboardScreenContent(
    uiState: ScoreboardUiState,
    onIncrement1: () -> Unit,
    onIncrement2: () -> Unit,
    onUndo: () -> Unit,
    onReset: () -> Unit,
    onRename1: (String) -> Unit,
    onRename2: (String) -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Dialog state for renaming
    var showRenameDialogForPlayer by remember { mutableStateOf<Int?>(null) }

    // Red and Blue Gradient Colors
    val redGradient = listOf(Color(0xFFE53935), Color(0xFFB71C1C))
    val blueGradient = listOf(Color(0xFF1E88E5), Color(0xFF0D47A1))

    // Resolve Localized Display Names (fallback to resource default strings)
    val displayName1 = uiState.name1 ?: stringResource(R.string.default_player_1)
    val displayName2 = uiState.name2 ?: stringResource(R.string.default_player_2)

    // Resolve Localized Status Text
    val statusText1 = when (uiState.status1) {
        MatchStatus.MATCH_POINT -> stringResource(R.string.status_match_point)
        MatchStatus.WINNER -> stringResource(R.string.status_winner)
        null -> null
    }

    val statusText2 = when (uiState.status2) {
        MatchStatus.MATCH_POINT -> stringResource(R.string.status_match_point)
        MatchStatus.WINNER -> stringResource(R.string.status_winner)
        null -> null
    }

    Box(modifier = modifier.fillMaxSize()) {
        val configuration = LocalConfiguration.current
        val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        val panelAlignment = if (isLandscape) Alignment.BottomCenter else Alignment.Center

        if (isLandscape) {
            Row(modifier = Modifier.fillMaxSize()) {
                PlayerHalf(
                    name = displayName1,
                    score = uiState.score1,
                    statusText = statusText1,
                    gradientColors = redGradient,
                    onClick = onIncrement1,
                    onRenameClick = { showRenameDialogForPlayer = 1 },
                    modifier = Modifier.weight(1f).fillMaxHeight()
                )
                PlayerHalf(
                    name = displayName2,
                    score = uiState.score2,
                    statusText = statusText2,
                    gradientColors = blueGradient,
                    onClick = onIncrement2,
                    onRenameClick = { showRenameDialogForPlayer = 2 },
                    modifier = Modifier.weight(1f).fillMaxHeight()
                )
            }
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                PlayerHalf(
                    name = displayName1,
                    score = uiState.score1,
                    statusText = statusText1,
                    gradientColors = redGradient,
                    onClick = onIncrement1,
                    onRenameClick = { showRenameDialogForPlayer = 1 },
                    modifier = Modifier.weight(1f).fillMaxWidth()
                )
                PlayerHalf(
                    name = displayName2,
                    score = uiState.score2,
                    statusText = statusText2,
                    gradientColors = blueGradient,
                    onClick = onIncrement2,
                    onRenameClick = { showRenameDialogForPlayer = 2 },
                    modifier = Modifier.weight(1f).fillMaxWidth()
                )
            }
        }

        // Control Capsule floating on the screen
        ControlPanel(
            canUndo = uiState.canUndo,
            onUndo = onUndo,
            onReset = onReset,
            onSettingsClick = onSettingsClick,
            modifier = Modifier.align(panelAlignment)
        )
    }

    // Rename Dialog Overlay
    if (showRenameDialogForPlayer != null) {
        val playerNum = showRenameDialogForPlayer!!
        val currentName = if (playerNum == 1) displayName1 else displayName2
        RenameDialog(
            initialName = currentName,
            onDismiss = { showRenameDialogForPlayer = null },
            onConfirm = { newName ->
                if (playerNum == 1) {
                    onRename1(newName)
                } else {
                    onRename2(newName)
                }
                showRenameDialogForPlayer = null
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ScoreboardPreview() {
    ProJudgeFeatherTheme {
        ScoreboardScreenContent(
            uiState = ScoreboardUiState(
                score1 = 15,
                score2 = 14,
                name1 = "Alpha",
                name2 = null,
                status1 = MatchStatus.MATCH_POINT,
                status2 = null,
                canUndo = true
            ),
            onIncrement1 = {},
            onIncrement2 = {},
            onUndo = {},
            onReset = {},
            onRename1 = {},
            onRename2 = {},
            onSettingsClick = {}
        )
    }
}
