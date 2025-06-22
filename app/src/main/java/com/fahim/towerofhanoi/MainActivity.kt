package com.fahim.towerofhanoi

import GameState
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.fahim.towerofhanoi.ui.theme.TowerOfHanoiTheme
import handleDrop
import handlePegClick
import resetGame

// MainActivity.kt
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TowerOfHanoiTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    TowerOfHanoiGame()
                }
            }
        }
    }
}

@Composable
fun TowerOfHanoiGame() {
    var state by remember { mutableStateOf(resetGame(3)) }
    val diskColors = remember {
        listOf(
            Color(0xFFF44336), // Red
            Color(0xFFFF9800), // Orange
            Color(0xFFFFEB3B), // Yellow
            Color(0xFF4CAF50), // Green
            Color(0xFF2196F3), // Blue
            Color(0xFF3F51B5), // Indigo
            Color(0xFF9C27B0), // Purple
            Color(0xFFE91E63)  // Pink
        )
    }
    var showHintDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Game controls
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Level selection
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Level:", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.width(8.dp))
                Row(
                    modifier = Modifier
                        .horizontalScroll(rememberScrollState()) // Makes the row scrollable
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    (3..8).forEach { level ->
                        Button(
                            onClick = { state = resetGame(level) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (state.numDisks == level) MaterialTheme.colorScheme.secondary
                                else MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Text("$level")
                        }
                    }
                }
            }

        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {


            // Moves counter
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Moves", style = MaterialTheme.typography.labelMedium)
                Text("${state.moves}", style = MaterialTheme.typography.titleLarge)
            }
            Button(
                onClick = { state = resetGame(state.numDisks) },
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Icon(Icons.Default.Refresh, contentDescription = "Reset")
                Spacer(modifier = Modifier.width(4.dp))
                Text("Reset")
            }
            // Optimal moves
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Optimal", style = MaterialTheme.typography.labelMedium)
                Text("${state.optimalMoves}", style = MaterialTheme.typography.titleLarge)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Game board
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Render each peg
                (0..2).forEach { pegIndex ->
                    PegView(
                        pegIndex = pegIndex,
                        disks = state.pegs[pegIndex],
                        isSelected = state.selectedPeg == pegIndex,
                        diskColors = diskColors,
                        numDisks = state.numDisks,
                        onPegClick = { state = handlePegClick(state, pegIndex) },
                        onDragStart = { diskSize ->
                            state = state.copy(
                                draggedDisk = GameState.DraggedDisk(diskSize, pegIndex),
                                selectedPeg = null
                            )
                        },
                        onDrop = { state = handleDrop(state, pegIndex) }
                    )
                }
            }

            // Render dragged disk
            state.draggedDisk?.let { draggedDisk ->
                val diskSize = draggedDisk.diskSize
                val diskWidth = 20.dp + diskSize * 10.dp
                var offsetX by remember { mutableStateOf(Offset.Zero) }

                Box(
                    modifier = Modifier
                        .offset {
                            IntOffset(
                                (offsetX.x - diskWidth.value / 2).toInt(),
                                offsetX.y.toInt()
                            )
                        }
                        .width(diskWidth)
                        .height(20.dp)
                        .background(
                            color = diskColors.getOrElse(diskSize - 1) { Color.Gray },
                            shape = RoundedCornerShape(10.dp)
                        )
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDrag = { change, _ ->
                                    offsetX += change.positionChange()
                                },
                                onDragEnd = {
                                    state = state.copy(draggedDisk = null)
                                }
                            )
                        }
                )
            }
        }
    }

    // Win dialog
    if (state.isWon) {
        AlertDialog(
            onDismissRequest = { state = resetGame(state.numDisks) },
            title = { Text("Congratulations!") },
            text = {
                Text(
                    "You solved the puzzle in ${state.moves} moves. " +
                            "The optimal number of moves was ${state.optimalMoves}."
                )
            },
            confirmButton = {
                Button(
                    onClick = { state = resetGame(state.numDisks) }
                ) {
                    Text("Play Again")
                }
            }
        )
    }

    // Hint dialog
    if (showHintDialog) {
        AlertDialog(
            onDismissRequest = { showHintDialog = false },
            title = { Text("Hint") },
            text = {
                Text(state.hint ?: "No hint available")
            },
            confirmButton = {
                Button(
                    onClick = { showHintDialog = false }
                ) {
                    Text("Close")
                }
            }
        )
    }
}

@Composable
fun PegView(
    pegIndex: Int,
    disks: List<Int>,
    isSelected: Boolean,
    diskColors: List<Color>,
    numDisks: Int,
    onPegClick: () -> Unit,
    onDragStart: (Int) -> Unit,
    onDrop: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(100.dp)
            .fillMaxHeight()
            .clickable { onPegClick() }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = {
                        if (disks.isNotEmpty()) {
                            onDragStart(disks.last())
                        }
                    },
                    onDrag = { _, _ -> },
                    onDragEnd = { onDrop() }
                )
            },
        contentAlignment = Alignment.BottomCenter
    ) {
        // Peg stand
        Box(
            modifier = Modifier
                .width(10.dp)
                .fillMaxHeight(0.8f)
                .background(Color.Gray)
        )

        // Peg base
        Box(
            modifier = Modifier
                .width(100.dp)
                .height(10.dp)
                .background(Color.Gray)
        )

        // Disks
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            disks.reversed().forEachIndexed { index, diskSize ->
                val diskWidth = 20.dp + diskSize * 10.dp
                val isTopDisk = index == 0

                Box(
                    modifier = Modifier
                        .width(diskWidth)
                        .height(20.dp)
                        .background(
                            color = diskColors.getOrElse(diskSize - 1) { Color.Gray },
                            shape = RoundedCornerShape(10.dp)
                        )
                        .border(
                            width = if (isSelected && isTopDisk) 2.dp else 0.dp,
                            color = Color.White,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .pointerInput(Unit) {
                            if (isTopDisk) {
                                detectDragGestures(
                                    onDragStart = { onDragStart(diskSize) },
                                    onDragEnd = { onDrop() },
                                    onDragCancel = {},
                                    onDrag = { _, _ -> }
                                )
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$diskSize",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun preview_TowerOfHanoi() {
    TowerOfHanoiGame()
}