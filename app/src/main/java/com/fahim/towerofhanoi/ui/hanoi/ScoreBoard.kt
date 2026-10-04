package com.fahim.towerofhanoi.ui.hanoi

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fahim.towerofhanoi.R
import java.util.Locale

private const val SECONDS_PER_MINUTE = 60

/** Formats elapsed seconds as `MM:SS` using ASCII digits regardless of locale. */
internal fun formatElapsed(totalSeconds: Int): String {
    val safe = totalSeconds.coerceAtLeast(0)
    return String.format(
        Locale.ROOT,
        "%02d:%02d",
        safe / SECONDS_PER_MINUTE,
        safe % SECONDS_PER_MINUTE,
    )
}

@Composable
internal fun ScoreBoard(
    moveCount: Int,
    optimalMoves: Int,
    elapsedSeconds: Int,
    efficiencyPercent: Int,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(16.dp)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .shadow(1.dp, shape)
            .background(Color.White, shape)
            .border(1.dp, HanoiColors.Slate200, shape)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        MovesStat(moveCount, optimalMoves)
        Box(
            Modifier
                .width(1.dp)
                .height(32.dp)
                .background(HanoiColors.Slate200),
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(horizontalAlignment = Alignment.End) {
                StatLabel(stringResource(R.string.label_time_elapsed))
                Text(
                    text = formatElapsed(elapsedSeconds),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = HanoiColors.Slate800,
                )
            }
            EfficiencyChip(efficiencyPercent, Modifier.padding(start = 12.dp))
        }
    }
}

@Composable
private fun MovesStat(moveCount: Int, optimalMoves: Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        val tileShape = RoundedCornerShape(12.dp)
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(HanoiColors.Purple50, tileShape)
                .border(1.dp, HanoiColors.Purple100, tileShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Rounded.Refresh,
                contentDescription = null,
                tint = HanoiColors.Primary,
                modifier = Modifier.size(20.dp),
            )
        }
        Column(Modifier.padding(start = 12.dp)) {
            StatLabel(stringResource(R.string.label_moves_optimal))
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(color = HanoiColors.Primary, fontSize = 20.sp)) {
                        append(moveCount.toString())
                    }
                    withStyle(
                        SpanStyle(
                            color = HanoiColors.Slate400,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                        ),
                    ) {
                        append(" / ")
                    }
                    withStyle(SpanStyle(color = HanoiColors.Slate700)) {
                        append(optimalMoves.toString())
                    }
                },
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold,
            )
        }
    }
}

@Composable
private fun EfficiencyChip(efficiencyPercent: Int, modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(8.dp)
    val description = stringResource(R.string.cd_efficiency, efficiencyPercent)
    Row(
        modifier = modifier
            .background(HanoiColors.Amber50, shape)
            .border(1.dp, HanoiColors.Amber200, shape)
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clearAndSetSemantics { contentDescription = description },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Filled.Star,
            contentDescription = null,
            tint = HanoiColors.Amber500,
            modifier = Modifier.size(16.dp),
        )
        Text(
            text = stringResource(R.string.efficiency_percent, efficiencyPercent),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = HanoiColors.Amber800,
            modifier = Modifier.padding(start = 4.dp),
        )
    }
}

@Composable
internal fun StatLabel(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text.uppercase(),
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 0.6.sp,
        color = HanoiColors.Slate400,
        modifier = modifier,
    )
}
