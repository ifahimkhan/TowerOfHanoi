package com.fahim.towerofhanoi.ui.hanoi

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fahim.towerofhanoi.R
import com.fahim.towerofhanoi.game.HanoiState
import com.fahim.towerofhanoi.game.pegLabel

private val ControlHeight = 48.dp
private val ControlShape = RoundedCornerShape(16.dp)

@Composable
internal fun ActionControls(
    onReset: () -> Unit,
    onUndo: () -> Unit,
    onHint: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Button(
            onClick = onReset,
            shape = ControlShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = HanoiColors.Slate900,
                contentColor = Color.White,
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 1.dp),
            modifier = Modifier
                .weight(1f)
                .height(ControlHeight),
        ) {
            ControlLabel(Icons.Rounded.Refresh, stringResource(R.string.action_reset))
        }
        OutlinedButton(
            onClick = onUndo,
            shape = ControlShape,
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = HanoiColors.SurfaceContainerHigh,
                contentColor = HanoiColors.Slate800,
            ),
            border = BorderStroke(1.dp, HanoiColors.Slate300),
            modifier = Modifier
                .weight(1f)
                .height(ControlHeight),
        ) {
            ControlLabel(HanoiIcons.Undo, stringResource(R.string.action_undo))
        }
        HintButton(onHint)
    }
}

@Composable
private fun RowScope.ControlLabel(icon: ImageVector, text: String) {
    Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(16.dp))
    Text(
        text = text,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        modifier = Modifier.padding(start = 8.dp),
    )
}

@Composable
private fun HintButton(onHint: () -> Unit) {
    val description = stringResource(R.string.cd_hint)
    Box(
        modifier = Modifier
            .size(ControlHeight)
            .clip(ControlShape)
            .background(HanoiColors.Purple50)
            .border(1.dp, HanoiColors.Purple200, ControlShape)
            .clickable(role = Role.Button, onClick = onHint)
            .semantics { contentDescription = description },
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = HanoiIcons.Lightbulb,
            contentDescription = null,
            tint = HanoiColors.Primary,
            modifier = Modifier.size(20.dp),
        )
    }
}

@Composable
internal fun RulesCard(modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(16.dp)
    val start = stringResource(R.string.peg_name, pegLabel(HanoiState.START_PEG))
    val goal = stringResource(R.string.peg_name, pegLabel(HanoiState.GOAL_PEG))
    val prefix = stringResource(R.string.rules_body_prefix)
    val middle = stringResource(R.string.rules_body_middle)
    val suffix = stringResource(R.string.rules_body_suffix)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(HanoiColors.SurfaceContainer, shape)
            .border(1.dp, HanoiColors.Slate200, shape)
            .padding(12.dp),
    ) {
        Icon(
            imageVector = Icons.Outlined.Info,
            contentDescription = null,
            tint = HanoiColors.Primary,
            modifier = Modifier
                .padding(top = 2.dp)
                .size(20.dp),
        )
        Column(Modifier.padding(start = 10.dp)) {
            Text(
                text = stringResource(R.string.rules_title),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = HanoiColors.Slate900,
            )
            Text(
                text = buildAnnotatedString {
                    append(prefix)
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append(start) }
                    append(middle)
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append(goal) }
                    append(suffix)
                },
                fontSize = 11.sp,
                lineHeight = 17.sp,
                color = HanoiColors.Slate600,
                modifier = Modifier.padding(top = 2.dp),
            )
        }
    }
}
