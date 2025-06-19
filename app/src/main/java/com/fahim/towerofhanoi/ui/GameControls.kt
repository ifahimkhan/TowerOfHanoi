package com.fahim.towerofhanoi.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun GameControls(
    onReset: () -> Unit,
    onUndo: () -> Unit,
    onLevelSelect: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(onClick = onReset) {
            Text("Reset")
        }
        
        Button(onClick = onUndo) {
            Text("Undo")
        }
        
        Button(onClick = onLevelSelect) {
            Text("Levels")
        }
    }
}

@Composable
fun GameWonDialog(moveCount: Int, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Congratulations!") },
        text = { Text("You solved the puzzle in $moveCount moves!") },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Play Again")
            }
        }
    )
}

@Composable
fun LevelSelectionDialog(
    onLevelSelected: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Difficulty") },
        text = {
            Column {
                (3..8).forEach { level ->
                    Button(
                        onClick = { onLevelSelected(level) },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                    ) {
                        Text("$level Disks (${(1 shl level) - 1} minimum moves)")
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}