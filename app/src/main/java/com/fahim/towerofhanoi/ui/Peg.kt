package com.fahim.towerofhanoi.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Peg(
    index: Int,
    disks: List<Int>,
    selectedPeg: Int?,
    onSelect: () -> Unit
) {
    Box(
        Modifier
            .width(100.dp)
            .fillMaxHeight()
            .clickable { onSelect() },
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            disks.forEachIndexed { i, size ->
                Disk(size = size, isTop = i == disks.lastIndex, isSelected = index == selectedPeg)
                Spacer(modifier = Modifier.height(4.dp))
            }
        }

        // Tower bar
        Box(
            modifier = Modifier
                .width(10.dp)
                .fillMaxHeight(0.9f)
                .background(Color.DarkGray)
                .align(Alignment.BottomCenter)
        )
    }
}
