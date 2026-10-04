package com.fahim.towerofhanoi.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation

// Tail tip position in head-radius units, measured from the head's center.
private const val TAIL_REACH_X = 1.5f
private const val TAIL_REACH_Y = 2.4f

/**
 * Comma-shaped tomoe: a round head centered at the origin with a tail that
 * sweeps up (-y) and curls right (+x). Head and tail are unioned so the
 * overlap never renders as a hole.
 */
internal fun tomoePath(headRadius: Float): Path {
    val r = headRadius
    val head = Path().apply { addOval(Rect(center = Offset.Zero, radius = r)) }
    val tail = Path().apply {
        moveTo(-r, 0f)
        cubicTo(-r, -1.5f * r, 0.2f * r, -2.3f * r, TAIL_REACH_X * r, -TAIL_REACH_Y * r)
        cubicTo(0.6f * r, -1.9f * r, -0.1f * r, -1.4f * r, 0f, -r)
        close()
    }
    return Path.combine(PathOperation.Union, head, tail)
}
