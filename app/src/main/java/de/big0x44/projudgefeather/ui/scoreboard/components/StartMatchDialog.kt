package de.big0x44.projudgefeather.ui.scoreboard.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import de.big0x44.projudgefeather.R
import de.big0x44.projudgefeather.model.Player

@Composable
fun StartMatchDialog(
    players: List<Player>,
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit,
    onAddPlayer: (String) -> Unit
) {
    var p1Selection by remember { mutableStateOf(players.getOrNull(0)?.name ?: "") }
    var p2Selection by remember { mutableStateOf(players.getOrNull(1)?.name ?: "") }
    var newPlayerName by remember { mutableStateOf("") }
    
    var p1Expanded by remember { mutableStateOf(false) }
    var p2Expanded by remember { mutableStateOf(false) }

    val isValid = p1Selection.isNotEmpty() && p2Selection.isNotEmpty() && p1Selection != p2Selection

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.start_match_title)) },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Player 1 Selection Dropdown
                Text(
                    text = stringResource(R.string.player1_select_label),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = p1Selection,
                        onValueChange = {},
                        readOnly = true,
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().clickable { p1Expanded = true },
                        enabled = false,
                        colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                            disabledTextColor = MaterialTheme.colorScheme.onSurface,
                            disabledBorderColor = MaterialTheme.colorScheme.outline,
                            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                    Box(modifier = Modifier.fillMaxWidth().height(56.dp).clickable { p1Expanded = true })
                    DropdownMenu(
                        expanded = p1Expanded,
                        onDismissRequest = { p1Expanded = false },
                        modifier = Modifier.fillMaxWidth(0.9f)
                    ) {
                        players.forEach { player ->
                            DropdownMenuItem(
                                text = { Text(player.name) },
                                onClick = {
                                    p1Selection = player.name
                                    p1Expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Player 2 Selection Dropdown
                Text(
                    text = stringResource(R.string.player2_select_label),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = p2Selection,
                        onValueChange = {},
                        readOnly = true,
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().clickable { p2Expanded = true },
                        enabled = false,
                        colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                            disabledTextColor = MaterialTheme.colorScheme.onSurface,
                            disabledBorderColor = MaterialTheme.colorScheme.outline,
                            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                    Box(modifier = Modifier.fillMaxWidth().height(56.dp).clickable { p2Expanded = true })
                    DropdownMenu(
                        expanded = p2Expanded,
                        onDismissRequest = { p2Expanded = false },
                        modifier = Modifier.fillMaxWidth(0.9f)
                    ) {
                        players.forEach { player ->
                            DropdownMenuItem(
                                text = { Text(player.name) },
                                onClick = {
                                    p2Selection = player.name
                                    p2Expanded = false
                                }
                            )
                        }
                    }
                }

                if (p1Selection.isNotEmpty() && p2Selection.isNotEmpty() && p1Selection == p2Selection) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(R.string.error_same_player),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Quick Add Player Fields
                Text(
                    text = stringResource(R.string.quick_add_player_label),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = newPlayerName,
                        onValueChange = { newPlayerName = it },
                        placeholder = { Text(stringResource(R.string.new_player_placeholder)) },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val trimmedName = newPlayerName.trim()
                            if (trimmedName.isNotEmpty()) {
                                onAddPlayer(trimmedName)
                                if (p1Selection.isEmpty()) {
                                    p1Selection = trimmedName
                                } else if (p2Selection.isEmpty()) {
                                    p2Selection = trimmedName
                                }
                                newPlayerName = ""
                            }
                        },
                        enabled = newPlayerName.trim().isNotEmpty()
                    ) {
                        Text(stringResource(R.string.add_button))
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (isValid) {
                        onConfirm(p1Selection, p2Selection)
                    }
                },
                enabled = isValid
            ) {
                Text(stringResource(R.string.start_button))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel_button))
            }
        }
    )
}
