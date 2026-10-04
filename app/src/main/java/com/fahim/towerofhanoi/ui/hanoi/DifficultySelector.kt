package com.fahim.towerofhanoi.ui.hanoi

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fahim.towerofhanoi.R
import com.fahim.towerofhanoi.game.MAX_DISKS
import com.fahim.towerofhanoi.game.MIN_DISKS

@Composable
internal fun DifficultySelector(
    selectedDiskCount: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(vertical = 4.dp)
            .selectableGroup(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        for (count in MIN_DISKS..MAX_DISKS) {
            DifficultyPill(
                diskCount = count,
                selected = count == selectedDiskCount,
                onClick = { onSelect(count) },
            )
        }
    }
}

@Composable
private fun DifficultyPill(diskCount: Int, selected: Boolean, onClick: () -> Unit) {
    Text(
        text = stringResource(R.string.disk_count, diskCount),
        fontSize = 12.sp,
        fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
        color = if (selected) HanoiColors.Slate50 else HanoiColors.Slate700,
        modifier = Modifier
            .shadow(if (selected) 4.dp else 0.dp, CircleShape)
            .clip(CircleShape)
            .background(if (selected) HanoiColors.Slate900 else HanoiColors.Slate200)
            .selectable(selected = selected, role = Role.Tab, onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
    )
}
