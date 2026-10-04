package com.fahim.towerofhanoi.ui.hanoi

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fahim.towerofhanoi.R

@Composable
internal fun HanoiTopBar(
    soundOn: Boolean,
    onToggleSound: () -> Unit,
    onShowRules: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .shadow(1.dp, CircleShape)
                    .background(HanoiColors.PrimaryContainer, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = HanoiIcons.Tower,
                    contentDescription = null,
                    tint = HanoiColors.OnPrimaryContainer,
                    modifier = Modifier.size(20.dp),
                )
            }
            Column(Modifier.padding(start = 8.dp)) {
                Text(
                    text = stringResource(R.string.hanoi_title),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = HanoiColors.Slate900,
                    modifier = Modifier.semantics { heading() },
                )
                Text(
                    text = stringResource(R.string.hanoi_subtitle).uppercase(),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 0.6.sp,
                    color = HanoiColors.Slate500,
                )
            }
        }
        Row {
            IconButton(onClick = onToggleSound) {
                Icon(
                    imageVector = if (soundOn) HanoiIcons.VolumeOn else HanoiIcons.VolumeOff,
                    contentDescription = stringResource(
                        if (soundOn) R.string.cd_sound_on else R.string.cd_sound_off,
                    ),
                    tint = HanoiColors.Slate600,
                    modifier = Modifier.size(20.dp),
                )
            }
            IconButton(onClick = onShowRules) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = stringResource(R.string.cd_rules),
                    tint = HanoiColors.Slate600,
                    modifier = Modifier.size(20.dp),
                )
            }
        }
    }
}
