package com.fahim.towerofhanoi.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TowersView(
    towers: List<List<Int>>,
    selectedDisk: Int?,
    onDiskSelected: (Int, Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        towers.forEachIndexed { towerIndex, disks ->
            TowerView(
                disks = disks,
                towerIndex = towerIndex,
                selectedDisk = selectedDisk,
                onDiskSelected = onDiskSelected
            )
        }
    }
}

@Composable
fun TowerView(
    disks: List<Int>,
    towerIndex: Int,
    selectedDisk: Int?,
    onDiskSelected: (Int, Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(100.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // This Box will contain both the peg and the disks
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            // Peg (positioned at the center)
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .width(10.dp)
                    .fillMaxHeight()
                    .background(Color.Gray)
            )

            // Disks (stacked from bottom to top)
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                disks.reversed().forEach { diskSize ->
                    Disk(
                        size = diskSize,
                        isSelected = selectedDisk == diskSize,
                        onClick = { onDiskSelected(towerIndex, diskSize) }
                    )
                }
            }
        }

        // Base
        Box(
            modifier = Modifier
                .width(100.dp)
                .height(10.dp)
                .background(Color.Gray)
        )
    }
}
@Composable
fun Disk(size: Int, isSelected: Boolean, onClick: () -> Unit) {
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