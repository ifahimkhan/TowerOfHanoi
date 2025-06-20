package com.fahim.towerofhanoi.ui

import GameState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import kotlin.math.roundToInt

// TowerView.kt
@Composable
fun TowerView(
    state: GameState,
    towerIndex: Int,
    onDragStart: (Int) -> Unit,
    onDrag: (Offset) -> Unit,
    onDragEnd: (Int) -> Unit,
    updateBounds: (Int, Rect) -> Unit,
    modifier: Modifier = Modifier
) {
    val towerDisks = state.towers[towerIndex]
    val isBeingDraggedFromHere = state.dragState?.currentTower == towerIndex
    val isDragSource = state.dragState?.currentTower == towerIndex
    val currentDisks = if (isBeingDraggedFromHere && towerDisks.isNotEmpty()) {
        towerDisks.dropLast(1)
    } else {
        towerDisks
    }

    // Track tower bounds for drop detection
    val towerBounds = remember { mutableStateOf(Rect.Zero) }

    Box(
        modifier = Modifier
            .width(100.dp)
            .fillMaxHeight()
            .padding(bottom = 48.dp)
            .onGloballyPositioned { coordinates ->
                updateBounds(towerIndex, coordinates.boundsInRoot())
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { onDragStart(towerIndex) },
                    onDrag = { change, _ -> onDrag(change.position) },
                    onDragEnd = { onDragEnd(towerIndex) }
                )
            },
        contentAlignment = Alignment.BottomCenter
    ) {
        // Peg
        Box(
            modifier = Modifier
                .width(10.dp)
                .fillMaxHeight(0.8f)
                .background(Color.Gray)
        )

        // Base
        Box(
            modifier = Modifier
                .width(100.dp)
                .height(10.dp)
                .background(Color.Gray)
                .align(Alignment.BottomCenter)
        )

        // Disks on tower
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            currentDisks.reversed().forEach { diskSize ->
                Disk(size = diskSize)
                Spacer(modifier = Modifier.height(2.dp))
            }
        }
    }
}

@Composable
fun DraggedDisk(state: GameState) {
    state.dragState?.let { dragState ->
        val density = LocalDensity.current
        val diskSizePx = with(density) { (20.dp + dragState.diskSize * 10.dp).toPx() }
        val offsetX = dragState.offset.x - diskSizePx / 2

        Box(
            modifier = Modifier
                .offset { IntOffset(offsetX.roundToInt(), dragState.offset.y.roundToInt()) }
                .width(20.dp + dragState.diskSize * 10.dp)
                .height(20.dp)
                .background(
                    color = diskColor(dragState.diskSize),
                    shape = RoundedCornerShape(10.dp)
                )
                .border(
                    width = 1.dp,
                    color = Color.White,
                    shape = RoundedCornerShape(10.dp)
                )
        )
    }
}

private fun diskColor(size: Int): Color {
    return when (size % 6) {
        1 -> Color(0xFFFF5252) // Red
        2 -> Color(0xFF448AFF) // Blue
        3 -> Color(0xFF69F0AE) // Green
        4 -> Color(0xFFFFD740) // Yellow
        5 -> Color(0xFFB388FF) // Purple
        else -> Color(0xFF7C4DFF) // Deep Purple
    }
}

@Composable
fun Disk(size: Int, isSelected: Boolean = false, onClick: () -> Unit = {}) {
    val diskWidth = 20.dp + (size * 10).dp
    val diskColor = when (size % 5) {
        1 -> Color.Red
        2 -> Color.Blue
        3 -> Color.Green
        4 -> Color.Yellow
        else -> Color.Magenta
    }

    Box(
        modifier = Modifier
            .width(diskWidth)
            .height(20.dp)
            .background(
                color = if (isSelected) diskColor.copy(alpha = 0.7f) else diskColor,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable { onClick() }
            .padding(vertical = 2.dp)
    )
}