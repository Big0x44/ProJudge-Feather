package de.big0x44.projudgefeather.ui.scoreboard

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import de.big0x44.projudgefeather.R
import de.big0x44.projudgefeather.model.MatchStatus
import de.big0x44.projudgefeather.ui.scoreboard.components.ControlPanel
import de.big0x44.projudgefeather.ui.scoreboard.components.PlayerHalf
import de.big0x44.projudgefeather.ui.scoreboard.components.StartMatchDialog
import de.big0x44.projudgefeather.ui.theme.ProJudgeFeatherTheme

@Composable
fun ScoreboardScreen(
    viewModel: ScoreboardViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    when (uiState.currentScreen) {
        AppScreen.SCOREBOARD -> {
            ScoreboardScreenContent(
                uiState = uiState,
                onIncrement1 = { viewModel.incrementScore1() },
                onIncrement2 = { viewModel.incrementScore2() },
                onUndo = { viewModel.undo() },
                onReset = { viewModel.reset() },
                onNewMatchClick = { viewModel.showStartMatchDialog(true) },
                onHistoryClick = { viewModel.navigateTo(AppScreen.HISTORY) },
                onDismissStartMatch = { viewModel.showStartMatchDialog(false) },
                onConfirmStartMatch = { p1, p2 -> viewModel.startMatch(p1, p2) },
                onAddPlayerStartMatch = { viewModel.addPlayer(it) },
                modifier = modifier
            )
        }
        AppScreen.HISTORY -> {
            HistoryScreen(
                matchHistory = uiState.matchHistory,
                onBack = { viewModel.navigateTo(AppScreen.SCOREBOARD) },
                onClearAll = { viewModel.clearMatchHistory() },
                onExport = { viewModel.exportHistory(it) },
                onImport = { viewModel.importHistory(it) },
                onNavigateToPlayers = { viewModel.navigateTo(AppScreen.PLAYERS) },
                modifier = modifier
            )
        }
        AppScreen.PLAYERS -> {
            PlayersScreen(
                players = uiState.players,
                onBack = { viewModel.navigateTo(AppScreen.SCOREBOARD) },
                onAddPlayer = { viewModel.addPlayer(it) },
                onDeletePlayer = { viewModel.deletePlayer(it) },
                onNavigateToHistory = { viewModel.navigateTo(AppScreen.HISTORY) },
                modifier = modifier
            )
        }
    }
}

@Composable
fun ScoreboardScreenContent(
    uiState: ScoreboardUiState,
    onIncrement1: () -> Unit,
    onIncrement2: () -> Unit,
    onUndo: () -> Unit,
    onReset: () -> Unit,
    onNewMatchClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onDismissStartMatch: () -> Unit,
    onConfirmStartMatch: (String, String) -> Unit,
    onAddPlayerStartMatch: (String) -> Unit,
    modifier: Modifier = Modifier
) {
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
                    modifier = Modifier.weight(1f).fillMaxHeight()
                )
                PlayerHalf(
                    name = displayName2,
                    score = uiState.score2,
                    statusText = statusText2,
                    gradientColors = blueGradient,
                    onClick = onIncrement2,
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
                    modifier = Modifier.weight(1f).fillMaxWidth()
                )
                PlayerHalf(
                    name = displayName2,
                    score = uiState.score2,
                    statusText = statusText2,
                    gradientColors = blueGradient,
                    onClick = onIncrement2,
                    modifier = Modifier.weight(1f).fillMaxWidth()
                )
            }
        }

        // Control Capsule floating on the screen
        ControlPanel(
            canUndo = uiState.canUndo,
            onUndo = onUndo,
            onReset = onReset,
            onNewMatchClick = onNewMatchClick,
            onHistoryClick = onHistoryClick,
            modifier = Modifier.align(panelAlignment)
        )
    }

    // Start New Match Dialog Overlay
    if (uiState.showStartMatchDialog) {
        StartMatchDialog(
            players = uiState.players,
            onDismiss = onDismissStartMatch,
            onConfirm = onConfirmStartMatch,
            onAddPlayer = onAddPlayerStartMatch
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
            onNewMatchClick = {},
            onHistoryClick = {},
            onDismissStartMatch = {},
            onConfirmStartMatch = { _, _ -> },
            onAddPlayerStartMatch = {}
        )
    }
}
