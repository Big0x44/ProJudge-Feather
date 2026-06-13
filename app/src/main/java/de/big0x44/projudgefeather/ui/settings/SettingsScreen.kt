package de.big0x44.projudgefeather.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.big0x44.projudgefeather.R
import de.big0x44.projudgefeather.ui.scoreboard.components.AddIcon
import de.big0x44.projudgefeather.ui.scoreboard.components.ArrowBackIcon
import de.big0x44.projudgefeather.ui.scoreboard.components.RemoveIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    var showPointsDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.settings_title),
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = ArrowBackIcon,
                            contentDescription = stringResource(R.string.back_description),
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF121212),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = Color(0xFF121212)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1E1E1E)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = stringResource(R.string.winning_points_label),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(R.string.winning_points_desc),
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(
                            onClick = { viewModel.updateWinningPoints(uiState.winningPoints - 1) },
                            enabled = uiState.winningPoints > 5,
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = Color(0xFF2D2D2D),
                                contentColor = Color.White,
                                disabledContainerColor = Color(0xFF2D2D2D).copy(alpha = 0.5f),
                                disabledContentColor = Color.White.copy(alpha = 0.3f)
                            ),
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                imageVector = RemoveIcon,
                                contentDescription = "Decrease",
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Text(
                            text = uiState.winningPoints.toString(),
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .width(80.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { showPointsDialog = true }
                                .padding(vertical = 4.dp)
                        )

                        IconButton(
                            onClick = { viewModel.updateWinningPoints(uiState.winningPoints + 1) },
                            enabled = uiState.winningPoints < 50,
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = Color(0xFF2D2D2D),
                                contentColor = Color.White,
                                disabledContainerColor = Color(0xFF2D2D2D).copy(alpha = 0.5f),
                                disabledContentColor = Color.White.copy(alpha = 0.3f)
                            ),
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                imageVector = AddIcon,
                                contentDescription = "Increase",
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    if (showPointsDialog) {
        PointsInputDialog(
            initialPoints = uiState.winningPoints,
            onDismiss = { showPointsDialog = false },
            onConfirm = { points ->
                viewModel.updateWinningPoints(points)
                showPointsDialog = false
            }
        )
    }
}

@Composable
fun PointsInputDialog(
    initialPoints: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    var text by remember { mutableStateOf(initialPoints.toString()) }
    val parsedPoints = text.toIntOrNull()
    val isValid = parsedPoints != null && parsedPoints in 5..50

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.enter_points_title)) },
        text = {
            Column {
                OutlinedTextField(
                    value = text,
                    onValueChange = { input ->
                        if (input.all { it.isDigit() } && input.length <= 3) {
                            text = input
                        }
                    },
                    singleLine = true,
                    label = { Text(stringResource(R.string.points_label)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = !isValid && text.isNotEmpty()
                )
                if (!isValid && text.isNotEmpty()) {
                    Text(
                        text = stringResource(R.string.points_error),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (isValid) {
                        onConfirm(parsedPoints!!)
                    }
                },
                enabled = isValid
            ) {
                Text(stringResource(R.string.save_button))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel_button))
            }
        }
    )
}
