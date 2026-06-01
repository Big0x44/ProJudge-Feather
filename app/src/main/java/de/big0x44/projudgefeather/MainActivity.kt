package de.big0x44.projudgefeather

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.big0x44.projudgefeather.ui.theme.ProJudgeFeatherTheme
import de.big0x44.projudgefeather.model.ScoreboardLogic

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProJudgeFeatherTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ScoreboardScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

// Custom Vectors for Icons to avoid external library dependencies
private val UndoIcon: ImageVector
    get() = ImageVector.Builder(
        name = "Undo",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.White)) {
            moveTo(12.5f, 8f)
            curveTo(9.85f, 8f, 7.45f, 9f, 5.6f, 10.6f)
            lineTo(2f, 7f)
            verticalLineTo(16f)
            horizontalLineTo(11f)
            lineTo(7.38f, 12.38f)
            curveTo(8.77f, 11.22f, 10.54f, 10.5f, 12.5f, 10.5f)
            curveTo(16.5f, 10.5f, 19.73f, 13.3f, 20.35f, 17.09f)
            lineTo(22.8f, 16.29f)
            curveTo(21.95f, 11.07f, 17.7f, 8f, 12.5f, 8f)
            close()
        }
    }.build()

private val EditIcon: ImageVector
    get() = ImageVector.Builder(
        name = "Edit",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.White)) {
            moveTo(3f, 17.25f)
            verticalLineTo(21f)
            horizontalLineTo(6.75f)
            lineTo(17.81f, 9.94f)
            lineTo(14.06f, 6.19f)
            lineTo(3f, 17.25f)
            close()
            moveTo(20.71f, 7.04f)
            curveTo(21.1f, 6.65f, 21.1f, 6.02f, 20.71f, 5.63f)
            lineTo(18.37f, 3.29f)
            curveTo(17.98f, 2.9f, 17.35f, 2.9f, 16.96f, 3.29f)
            lineTo(15.13f, 5.12f)
            lineTo(18.88f, 8.87f)
            lineTo(20.71f, 7.04f)
            close()
        }
    }.build()

private val RefreshIcon: ImageVector
    get() = ImageVector.Builder(
        name = "Refresh",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.White)) {
            moveTo(17.65f, 6.35f)
            curveTo(16.2f, 4.9f, 14.21f, 4f, 12f, 4f)
            curveTo(7.58f, 4f, 4.01f, 7.58f, 4.01f, 12f)
            curveTo(4.01f, 16.42f, 7.58f, 20f, 12f, 20f)
            curveTo(15.73f, 20f, 18.84f, 17.45f, 19.73f, 14f)
            horizontalLineTo(17.65f)
            curveTo(16.83f, 16.33f, 14.61f, 18f, 12f, 18f)
            curveTo(8.69f, 18f, 6f, 15.31f, 6f, 12f)
            curveTo(6f, 8.69f, 8.69f, 6f, 12f, 6f)
            curveTo(13.66f, 6f, 15.14f, 6.69f, 16.22f, 7.78f)
            lineTo(13f, 11f)
            horizontalLineTo(20f)
            verticalLineTo(4f)
            lineTo(17.65f, 6.35f)
            close()
        }
    }.build()

@Composable
fun ScoreboardScreen(modifier: Modifier = Modifier) {
    // Score state
    var score1 by rememberSaveable { mutableIntStateOf(0) }
    var score2 by rememberSaveable { mutableIntStateOf(0) }

    // Player Names
    var name1 by rememberSaveable { mutableStateOf("Player 1") }
    var name2 by rememberSaveable { mutableStateOf("Player 2") }

    // History to support Undo (saves pairs of scores)
    val history = remember { mutableStateListOf<Pair<Int, Int>>() }

    // Dialog state for renaming
    var showRenameDialogForPlayer by remember { mutableStateOf<Int?>(null) }

    fun addHistoryState() {
        if (history.size >= 50) {
            history.removeAt(0)
        }
        history.add(score1 to score2)
    }

    fun incrementScore1() {
        addHistoryState()
        score1++
    }

    fun incrementScore2() {
        addHistoryState()
        score2++
    }

    val status1 = ScoreboardLogic.getPlayerStatus(score1, score2)
    val status2 = ScoreboardLogic.getPlayerStatus(score2, score1)

    // Red and Blue Gradient Colors
    val redGradient = listOf(Color(0xFFE53935), Color(0xFFB71C1C))
    val blueGradient = listOf(Color(0xFF1E88E5), Color(0xFF0D47A1))

    Box(modifier = modifier.fillMaxSize()) {
        val configuration = LocalConfiguration.current
        val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        val panelAlignment = if (isLandscape) Alignment.BottomCenter else Alignment.Center

        if (isLandscape) {
            Row(modifier = Modifier.fillMaxSize()) {
                PlayerHalf(
                    name = name1,
                    score = score1,
                    status = status1,
                    gradientColors = redGradient,
                    onClick = ::incrementScore1,
                    onRenameClick = { showRenameDialogForPlayer = 1 },
                    modifier = Modifier.weight(1f).fillMaxHeight()
                )
                PlayerHalf(
                    name = name2,
                    score = score2,
                    status = status2,
                    gradientColors = blueGradient,
                    onClick = ::incrementScore2,
                    onRenameClick = { showRenameDialogForPlayer = 2 },
                    modifier = Modifier.weight(1f).fillMaxHeight()
                )
            }
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                PlayerHalf(
                    name = name1,
                    score = score1,
                    status = status1,
                    gradientColors = redGradient,
                    onClick = ::incrementScore1,
                    onRenameClick = { showRenameDialogForPlayer = 1 },
                    modifier = Modifier.weight(1f).fillMaxWidth()
                )
                PlayerHalf(
                    name = name2,
                    score = score2,
                    status = status2,
                    gradientColors = blueGradient,
                    onClick = ::incrementScore2,
                    onRenameClick = { showRenameDialogForPlayer = 2 },
                    modifier = Modifier.weight(1f).fillMaxWidth()
                )
            }
        }

        // Control Capsule floating on the screen (Centered in Portrait, Bottom-Centered in Landscape)
        ControlPanel(
            canUndo = history.isNotEmpty(),
            onUndo = {
                if (history.isNotEmpty()) {
                    val prev = history.removeAt(history.size - 1)
                    score1 = prev.first
                    score2 = prev.second
                }
            },
            onReset = {
                addHistoryState()
                score1 = 0
                score2 = 0
            },
            modifier = Modifier.align(panelAlignment)
        )
    }

    // Rename Dialog Overlay
    if (showRenameDialogForPlayer != null) {
        val playerNum = showRenameDialogForPlayer!!
        val currentName = if (playerNum == 1) name1 else name2
        RenameDialog(
            initialName = currentName,
            onDismiss = { showRenameDialogForPlayer = null },
            onConfirm = { newName ->
                val cleanedName = newName.trim()
                if (playerNum == 1) {
                    name1 = if (cleanedName.isNotEmpty()) cleanedName else "Player 1"
                } else {
                    name2 = if (cleanedName.isNotEmpty()) cleanedName else "Player 2"
                }
                showRenameDialogForPlayer = null
            }
        )
    }
}

@Composable
fun PlayerHalf(
    name: String,
    score: Int,
    status: String?,
    gradientColors: List<Color>,
    onClick: () -> Unit,
    onRenameClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(Brush.linearGradient(gradientColors))
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeContent)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Player Name & Edit Button
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onRenameClick() }
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = name,
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = EditIcon,
                    contentDescription = "Rename",
                    tint = Color.White.copy(alpha = 0.6f),
                    modifier = Modifier.size(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Score with animated content transitions (slide and fade)
            AnimatedContent(
                targetState = score,
                transitionSpec = {
                    if (targetState > initialState) {
                        (slideInVertically { height -> height } + fadeIn()) togetherWith
                                (slideOutVertically { height -> -height } + fadeOut())
                    } else {
                        (slideInVertically { height -> -height } + fadeIn()) togetherWith
                                (slideOutVertically { height -> height } + fadeOut())
                    }.using(
                        SizeTransform(clip = false)
                    )
                },
                label = "ScoreAnimation"
            ) { targetScore ->
                Text(
                    text = targetScore.toString(),
                    style = TextStyle(
                        fontSize = 130.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        shadow = Shadow(
                            color = Color.Black.copy(alpha = 0.25f),
                            offset = Offset(2f, 4f),
                            blurRadius = 8f
                        )
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Pulse Animation for Match Point/Winner Status Badge
            if (status != null) {
                val infiniteTransition = rememberInfiniteTransition(label = "pulse")
                val scale by infiniteTransition.animateFloat(
                    initialValue = 0.95f,
                    targetValue = 1.05f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(1000, easing = FastOutSlowInEasing),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "scale"
                )

                val badgeBgColor = if (status == "WINNER") Color(0xFF4CAF50) else Color(0xFFFF9800)

                Surface(
                    color = badgeBgColor,
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .scale(scale)
                        .shadow(6.dp, shape = RoundedCornerShape(50)),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f))
                ) {
                    Text(
                        text = status,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }
            } else {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun ControlPanel(
    canUndo: Boolean,
    onUndo: () -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showResetConfirm by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier
            .padding(16.dp)
            .shadow(12.dp, shape = RoundedCornerShape(50))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { /* Consume clicks to prevent score increment */ },
        color = Color(0xDD1A1A1A),
        shape = RoundedCornerShape(50),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.15f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(
                onClick = onUndo,
                enabled = canUndo,
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = Color.White,
                    disabledContentColor = Color.White.copy(alpha = 0.3f)
                )
            ) {
                Icon(
                    imageVector = UndoIcon,
                    contentDescription = "Undo",
                    modifier = Modifier.size(22.dp)
                )
            }

            Box(
                modifier = Modifier
                    .size(width = 1.dp, height = 24.dp)
                    .background(Color.White.copy(alpha = 0.15f))
            )

            IconButton(
                onClick = { showResetConfirm = true },
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = Color.White
                )
            ) {
                Icon(
                    imageVector = RefreshIcon,
                    contentDescription = "Reset Match",
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }

    if (showResetConfirm) {
        AlertDialog(
            onDismissRequest = { showResetConfirm = false },
            title = { Text("Reset Score?") },
            text = { Text("Are you sure you want to reset the current match scores to 0?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onReset()
                        showResetConfirm = false
                    }
                ) {
                    Text("Reset", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetConfirm = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun RenameDialog(
    initialName: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var text by remember { mutableStateOf(initialName) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Rename Player") },
        text = {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                singleLine = true,
                label = { Text("Player Name") }
            )
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(text) }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ScoreboardPreview() {
    ProJudgeFeatherTheme {
        ScoreboardScreen()
    }
}