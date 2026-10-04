package com.fahim.towerofhanoi.ui

import androidx.annotation.IntRange
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fahim.towerofhanoi.R
import kotlin.math.cos
import kotlin.math.sin

enum class SharinganStyle { THREE_TOMOE, MANGEKYOU }

val SharinganRed = Color(0xFFC62828)

private const val MARK_COUNT = 3
private const val FIRST_MARK_DEGREES = -90f
private const val HALF_TURN_DEGREES = 180f
private const val FULL_TURN_DEGREES = 360f
private const val DEFAULT_REVOLUTION_MS = 1_200

// Proportions as fractions of the eye radius.
private const val OUTER_RING_WIDTH = 0.06f
private const val ORBIT_RADIUS = 0.55f
private const val ORBIT_RING_WIDTH = 0.015f
private const val ORBIT_RING_ALPHA = 0.6f
private const val TOMOE_HEAD = 0.12f
private const val PUPIL_RADIUS = 0.17f
private const val BLADE_TIP_RADIUS = 0.9f
private const val BLADE_OUTER_CONTROL = 0.75f
private const val BLADE_OUTER_SWEEP_DEGREES = -50f
private const val BLADE_INNER_CONTROL = 0.45f
private const val BLADE_INNER_SWEEP_DEGREES = -15f
private const val MANGEKYOU_CORE_RADIUS = 0.22f
private const val MANGEKYOU_DOT_RADIUS = 0.07f
private const val SHADE_ALPHA = 0.35f

/** Evenly spaced mark angles in degrees, starting at 12 o'clock and going clockwise. */
internal fun markAngles(count: Int = MARK_COUNT): List<Float> {
    require(count > 0) { "count must be positive, was $count" }
    return List(count) { FIRST_MARK_DEGREES + it * FULL_TURN_DEGREES / count }
}

/** Static Sharingan eye. Paths are cached per size, so redraws allocate nothing. */
@Composable
fun SharinganEye(
    modifier: Modifier = Modifier,
    style: SharinganStyle = SharinganStyle.THREE_TOMOE,
    irisColor: Color = SharinganRed,
    markColor: Color = Color.Black,
) {
    Spacer(
        modifier.drawWithCache {
            val radius = size.minDimension / 2
            val center = size.center
            val ringWidth = radius * OUTER_RING_WIDTH
            val marks = when (style) {
                SharinganStyle.THREE_TOMOE -> threeTomoePath(center, radius)
                SharinganStyle.MANGEKYOU -> mangekyouPath(center, radius)
            }
            val shade = Brush.radialGradient(
                colors = listOf(Color.Transparent, Color.Black.copy(alpha = SHADE_ALPHA)),
                center = center,
                radius = radius,
            )
            onDrawBehind {
                drawCircle(irisColor, radius, center)
                drawCircle(shade, radius, center)
                drawCircle(markColor, radius - ringWidth / 2, center, style = Stroke(ringWidth))
                if (style == SharinganStyle.THREE_TOMOE) {
                    drawCircle(
                        color = markColor.copy(alpha = ORBIT_RING_ALPHA),
                        radius = radius * ORBIT_RADIUS,
                        center = center,
                        style = Stroke(radius * ORBIT_RING_WIDTH),
                    )
                }
                drawPath(marks, markColor)
                if (style == SharinganStyle.MANGEKYOU) {
                    drawCircle(irisColor, radius * MANGEKYOU_DOT_RADIUS, center)
                }
            }
        },
    )
}

/**
 * Indeterminate loading indicator: a spinning Sharingan. The rotation is
 * applied in the graphics layer, so animating it never recomposes.
 */
@Composable
fun SharinganLoader(
    modifier: Modifier = Modifier,
    style: SharinganStyle = SharinganStyle.THREE_TOMOE,
    size: Dp = 64.dp,
    @IntRange(from = 1) revolutionMillis: Int = DEFAULT_REVOLUTION_MS,
) {
    require(revolutionMillis > 0) { "revolutionMillis must be positive, was $revolutionMillis" }
    val rotation = rememberInfiniteTransition(label = "sharingan").animateFloat(
        initialValue = 0f,
        // Counter-clockwise, so the tomoe heads lead and the tails trail.
        targetValue = -FULL_TURN_DEGREES,
        animationSpec = infiniteRepeatable(tween(revolutionMillis, easing = LinearEasing)),
        label = "sharinganRotation",
    )
    val description = stringResource(R.string.cd_loading)
    SharinganEye(
        style = style,
        modifier = modifier
            .size(size)
            .semantics {
                contentDescription = description
                progressBarRangeInfo = ProgressBarRangeInfo.Indeterminate
            }
            .graphicsLayer { rotationZ = rotation.value },
    )
}

/** Pupil plus three tomoe on the orbit ring, tails pointing clockwise along it. */
private fun threeTomoePath(center: Offset, radius: Float): Path {
    val marks = Path().apply { addOval(Rect(center, radius * PUPIL_RADIUS)) }
    val tomoe = tomoePath(radius * TOMOE_HEAD)
    for (angle in markAngles()) {
        val position = polar(center, radius * ORBIT_RADIUS, angle)
        val placement = Matrix().apply {
            translate(position.x, position.y)
            // The tomoe tail points along local -y; turn it onto the orbit's tangent.
            rotateZ(angle + HALF_TURN_DEGREES)
        }
        val placed = Path().apply {
            addPath(tomoe)
            transform(placement)
        }
        marks.addPath(placed)
    }
    return marks
}

/** Itachi-style pinwheel: three curved blades around a solid core. */
private fun mangekyouPath(center: Offset, radius: Float): Path {
    val core = Path().apply { addOval(Rect(center, radius * MANGEKYOU_CORE_RADIUS)) }
    val blades = Path()
    for (angle in markAngles()) {
        val tip = polar(center, radius * BLADE_TIP_RADIUS, angle)
        val outer = polar(center, radius * BLADE_OUTER_CONTROL, angle + BLADE_OUTER_SWEEP_DEGREES)
        val inner = polar(center, radius * BLADE_INNER_CONTROL, angle + BLADE_INNER_SWEEP_DEGREES)
        blades.moveTo(center.x, center.y)
        blades.quadraticBezierTo(outer.x, outer.y, tip.x, tip.y)
        blades.quadraticBezierTo(inner.x, inner.y, center.x, center.y)
        blades.close()
    }
    // Union, not addPath: opposite winding would punch holes where blades cross the core.
    return Path.combine(PathOperation.Union, core, blades)
}

private fun polar(center: Offset, distance: Float, degrees: Float): Offset {
    val radians = Math.toRadians(degrees.toDouble())
    return Offset(
        x = center.x + distance * cos(radians).toFloat(),
        y = center.y + distance * sin(radians).toFloat(),
    )
}

@Preview
@Composable
private fun SharinganPreview() {
    Row(
        modifier = Modifier
            .background(Color(0xFF111111))
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SharinganEye(Modifier.size(96.dp))
        SharinganEye(Modifier.size(96.dp), style = SharinganStyle.MANGEKYOU)
        SharinganLoader(size = 48.dp)
    }
}
