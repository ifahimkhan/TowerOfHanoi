package com.fahim.towerofhanoi.ui.hanoi

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fahim.towerofhanoi.R
import com.fahim.towerofhanoi.game.HanoiState
import com.fahim.towerofhanoi.game.PEG_COUNT
import com.fahim.towerofhanoi.game.pegLabel

private const val MIN_DISK_WIDTH_FRACTION = 0.28f
private const val MAX_DISK_WIDTH_FRACTION = 0.90f
private const val COMPACT_DISK_THRESHOLD = 7
private const val LIFTED_SCALE = 1.03f
private const val TOAST_FADE_MS = 300

private val PegAreaHeight = 224.dp
private val RodHeight = 192.dp
private val DiskHeight = 24.dp
private val CompactDiskHeight = 20.dp
private val DiskGap = 6.dp
private val LiftOffset = 24.dp

/** Event-driven board message. Identity equality, so repeating the same text re-triggers it. */
internal class ToastMessage(@StringRes val resId: Int, vararg val args: Any)

/** Width of a disk as a fraction of its peg column: smallest 28%, largest 90%. */
internal fun diskWidthFraction(size: Int, diskCount: Int): Float {
    val steps = (diskCount - 1).coerceAtLeast(1)
    val progress = (size - 1).toFloat() / steps
    return MIN_DISK_WIDTH_FRACTION + progress * (MAX_DISK_WIDTH_FRACTION - MIN_DISK_WIDTH_FRACTION)
}

@Composable
internal fun HanoiBoard(
    state: HanoiState,
    toast: ToastMessage?,
    onPegTap: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(24.dp)
    val diskHeight = if (state.diskCount >= COMPACT_DISK_THRESHOLD) CompactDiskHeight else DiskHeight
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(1.dp, shape)
            .background(Brush.verticalGradient(listOf(Color.White, HanoiColors.Slate50)), shape)
            .border(1.dp, HanoiColors.Slate200, shape)
            .padding(16.dp),
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(PegAreaHeight)
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                for (peg in 0 until PEG_COUNT) {
                    PegColumn(
                        peg = peg,
                        disks = state.pegs[peg],
                        diskCount = state.diskCount,
                        diskHeight = diskHeight,
                        isSelected = state.selectedPeg == peg,
                        onTap = { onPegTap(peg) },
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                    )
                }
            }
            BaseStand()
            PegLabels(Modifier.padding(top = 8.dp))
        }
        BoardToast(
            toast = toast,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 4.dp),
        )
    }
}

@Composable
private fun PegColumn(
    peg: Int,
    disks: List<Int>,
    diskCount: Int,
    diskHeight: Dp,
    isSelected: Boolean,
    onTap: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(16.dp)
    val ring by animateColorAsState(
        targetValue = if (isSelected) HanoiColors.Primary.copy(alpha = 0.6f) else Color.Transparent,
        label = "pegRing",
    )
    val fill by animateColorAsState(
        targetValue = if (isSelected) HanoiColors.Primary.copy(alpha = 0.05f) else Color.Transparent,
        label = "pegFill",
    )
    val label = pegLabel(peg)
    val description = if (isSelected) {
        stringResource(R.string.cd_peg_selected, label, disks.size)
    } else {
        stringResource(R.string.cd_peg, label, disks.size)
    }
    Box(
        modifier = modifier
            .clip(shape)
            .background(fill)
            .border(2.dp, ring, shape)
            .clickable(role = Role.Button, onClick = onTap)
            .clearAndSetSemantics { contentDescription = description },
        contentAlignment = Alignment.BottomCenter,
    ) {
        PegRod()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 2.dp),
            verticalArrangement = Arrangement.spacedBy(DiskGap, Alignment.Bottom),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            disks.asReversed().forEachIndexed { indexFromTop, size ->
                key(size) {
                    DiskView(
                        size = size,
                        widthFraction = diskWidthFraction(size, diskCount),
                        height = diskHeight,
                        lifted = isSelected && indexFromTop == 0,
                    )
                }
            }
        }
    }
}

@Composable
private fun PegRod() {
    val rodBrush = remember {
        Brush.horizontalGradient(listOf(HanoiColors.RodDark, HanoiColors.RodLight, HanoiColors.RodShade))
    }
    Box(
        Modifier
            .width(10.dp)
            .height(RodHeight)
            .shadow(2.dp, CircleShape)
            .background(rodBrush, CircleShape),
    )
}

@Composable
private fun DiskView(size: Int, widthFraction: Float, height: Dp, lifted: Boolean) {
    val palette = diskPalette(size)
    val bouncy = spring<Dp>(dampingRatio = 0.55f, stiffness = Spring.StiffnessMediumLow)
    val lift by animateDpAsState(if (lifted) LiftOffset else 0.dp, bouncy, label = "diskLift")
    val elevation by animateDpAsState(if (lifted) 12.dp else 1.dp, label = "diskElevation")
    val scale by animateFloatAsState(if (lifted) LIFTED_SCALE else 1f, label = "diskScale")
    Box(
        modifier = Modifier
            .fillMaxWidth(widthFraction)
            .height(height)
            .offset { IntOffset(0, -lift.roundToPx()) }
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .shadow(elevation, CircleShape)
            .background(palette.fill, CircleShape)
            .border(2.dp, palette.border, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            Modifier
                .size(8.dp)
                .background(Color.White.copy(alpha = 0.4f), CircleShape),
        )
    }
}

@Composable
private fun BaseStand() {
    val shape = RoundedCornerShape(12.dp)
    val woodBrush = remember {
        Brush.horizontalGradient(listOf(HanoiColors.BaseDark, HanoiColors.BaseMid, HanoiColors.BaseShade))
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(20.dp)
            .shadow(4.dp, shape)
            .background(woodBrush, shape)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(PEG_COUNT) {
            Box(
                Modifier
                    .width(40.dp)
                    .height(4.dp)
                    .background(HanoiColors.Amber200.copy(alpha = 0.2f), CircleShape),
            )
        }
    }
}

@Composable
private fun PegLabels(modifier: Modifier = Modifier) {
    Row(modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        for (peg in 0 until PEG_COUNT) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.peg_name, pegLabel(peg)).uppercase(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.6.sp,
                    color = HanoiColors.Slate500,
                )
                when (peg) {
                    HanoiState.START_PEG -> PegTag(
                        text = stringResource(R.string.peg_start),
                        background = HanoiColors.Slate200,
                        color = HanoiColors.Slate600,
                    )
                    HanoiState.GOAL_PEG -> PegTag(
                        text = stringResource(R.string.peg_goal),
                        background = HanoiColors.Purple100,
                        color = HanoiColors.Purple700,
                    )
                }
            }
        }
    }
}

@Composable
private fun PegTag(text: String, background: Color, color: Color) {
    Text(
        text = text,
        fontSize = 9.sp,
        color = color,
        modifier = Modifier
            .padding(start = 4.dp)
            .background(background, RoundedCornerShape(4.dp))
            .padding(horizontal = 4.dp),
    )
}

@Composable
private fun BoardToast(toast: ToastMessage?, modifier: Modifier = Modifier) {
    // AnimatedContent keeps the outgoing message composed while it fades out.
    AnimatedContent(
        targetState = toast,
        transitionSpec = { fadeIn(tween(TOAST_FADE_MS)) togetherWith fadeOut(tween(TOAST_FADE_MS)) },
        modifier = modifier,
        label = "boardToast",
    ) { message ->
        if (message == null) return@AnimatedContent
        Text(
            text = stringResource(message.resId, *message.args),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .widthIn(max = 280.dp)
                .shadow(8.dp, CircleShape)
                .background(HanoiColors.Slate900.copy(alpha = 0.9f), CircleShape)
                .padding(horizontal = 12.dp, vertical = 6.dp)
                .semantics { liveRegion = LiveRegionMode.Polite },
        )
    }
}
