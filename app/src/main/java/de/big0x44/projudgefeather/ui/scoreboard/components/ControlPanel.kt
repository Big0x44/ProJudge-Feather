package de.big0x44.projudgefeather.ui.scoreboard.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import de.big0x44.projudgefeather.R

@Composable
fun ControlPanel(
    canUndo: Boolean,
    onUndo: () -> Unit,
    onReset: () -> Unit,
    onSettingsClick: () -> Unit,
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
                    contentDescription = stringResource(R.string.undo_description),
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
                    contentDescription = stringResource(R.string.reset_description),
                    modifier = Modifier.size(22.dp)
                )
            }

            Box(
                modifier = Modifier
                    .size(width = 1.dp, height = 24.dp)
                    .background(Color.White.copy(alpha = 0.15f))
            )

            IconButton(
                onClick = onSettingsClick,
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = Color.White
                )
            ) {
                Icon(
                    imageVector = SettingsIcon,
                    contentDescription = stringResource(R.string.settings_description),
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }

    if (showResetConfirm) {
        AlertDialog(
            onDismissRequest = { showResetConfirm = false },
            title = { Text(stringResource(R.string.reset_dialog_title)) },
            text = { Text(stringResource(R.string.reset_dialog_text)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        onReset()
                        showResetConfirm = false
                    }
                ) {
                    Text(stringResource(R.string.reset_confirm), color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetConfirm = false }) {
                    Text(stringResource(R.string.cancel_button))
                }
            }
        )
    }
}
