package com.fahim.towerofhanoi.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Disk(size: Int, isTop: Boolean, isSelected: Boolean) {
    val diskColor = when (size % 6) {
        1 -> Color.Red
        2 -> Color.Yellow
        3 -> Color.Green
        4 -> Color.Blue
        5 -> Color.Magenta
        else -> Color.Cyan
    }

    Box(
        modifier = Modifier
            .width((30 + size * 20).dp)
            .height(24.dp)
            .background(
                color = if (isSelected && isTop) diskColor.copy(alpha = 0.7f) else diskColor,
                shape = RoundedCornerShape(8.dp)
            )
            .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
    )
}
