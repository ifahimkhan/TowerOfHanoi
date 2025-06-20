package com.fahim.towerofhanoi

import GameState
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fahim.towerofhanoi.ui.DraggedDisk
import com.fahim.towerofhanoi.ui.TowerView
import handleDrag
import handleDragEnd
import handleDragStart

// MainActivity.kt
@Composable
fun TowerOfHanoiGame() {
    var gameState by remember { mutableStateOf(GameState.initial()) }
    var showDiskSelection by remember { mutableStateOf(false) }
    val towerBounds = remember { mutableStateOf(listOf(Rect.Zero, Rect.Zero, Rect.Zero)) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Game header
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 48.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Moves: ${gameState.moveCount}", style = MaterialTheme.typography.titleMedium)
                Button(onClick = { showDiskSelection = true }) {
                    Text("Change Disks")
                }
                Button(onClick = { gameState = GameState.initial(gameState.diskCount) }) {
                    Text("Reset")
                }
            }

            // Towers area
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDrag = { change, _ ->
                                gameState = handleDrag(gameState, change.position)
                            },
                            onDragEnd = {
                                gameState =
                                    handleDragEnd(gameState, Offset.Zero, towerBounds.value) // Cancel if dropped outside
                            }
                        )
                    }
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    var myOffset by remember { mutableStateOf(Offset.Zero) }
                    for (towerIndex in 0..2) {
                        TowerView(
                            state = gameState,
                            towerIndex = towerIndex,
                            onDragStart = { gameState = handleDragStart(gameState, towerIndex) },
                            onDrag = { offset ->
                                myOffset = offset
                                gameState = handleDrag(gameState, offset)
                            },
                            onDragEnd = { offset ->
                                gameState = handleDragEnd(
                                    state = gameState,
                                    dropPosition = myOffset,
                                    towerBounds = towerBounds.value
                                )
                            },
                            updateBounds = { index, bounds ->
                                towerBounds.value = towerBounds.value.toMutableList().apply {
                                    set(index, bounds)
                                }
                            },
                            modifier = Modifier.onGloballyPositioned { coordinates ->
                                towerBounds.value = towerBounds.value.toMutableList().apply {
                                    set(towerIndex, coordinates.boundsInRoot())
                                }
                            }
                        )
                    }
                }

                // Dragged disk (floating above towers)
                DraggedDisk(state = gameState)
            }
        }

        // Win dialog
        if (gameState.isWon) {
            AlertDialog(
                onDismissRequest = { gameState = GameState.initial(gameState.diskCount) },
                title = { Text("You Won!") },
                text = { Text("Completed in ${gameState.moveCount} moves!") },
                confirmButton = {
                    Button(onClick = { gameState = GameState.initial(gameState.diskCount) }) {
                        Text("Play Again")
                    }
                }
            )
        }

        // Disk selection dialog
        if (showDiskSelection) {
            AlertDialog(
                onDismissRequest = { showDiskSelection = false },
                title = { Text("Select Disk Count") },
                text = {
                    Column {
                        (3..6).forEach { count ->
                            Button(
                                onClick = {
                                    gameState = GameState.initial(count)
                                    showDiskSelection = false
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("$count Disks")
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                },
                confirmButton = {
                    Button(onClick = { showDiskSelection = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun preview_TowerOfHanoi() {
    TowerOfHanoiGame()
}