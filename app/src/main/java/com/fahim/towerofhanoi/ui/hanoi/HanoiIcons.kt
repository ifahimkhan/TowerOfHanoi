package com.fahim.towerofhanoi.ui.hanoi

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.unit.dp

/**
 * Outline icons ported from the design's SVGs (24x24, 2px round strokes).
 * Arc flags are space-separated so the path parser reads them reliably.
 */
internal object HanoiIcons {
    val Tower: ImageVector = strokeIcon("Tower", "M4 20h16M7 16h10M9 12h6M12 4v16")

    val Undo: ImageVector = strokeIcon("Undo", "M3 10h10a5 5 0 0 1 5 5v2m-15-7l4-4m-4 4l4 4")

    val VolumeOn: ImageVector = strokeIcon(
        "VolumeOn",
        "M15.536 8.464a5 5 0 0 1 0 7.072M18.364 5.636a9 9 0 0 1 0 12.728" +
            "M11 5L6 9H2v6h4l5 4V5z",
    )

    val VolumeOff: ImageVector = strokeIcon("VolumeOff", "M11 5L6 9H2v6h4l5 4V5zM16 9l5 6M21 9l-5 6")

    val Lightbulb: ImageVector = strokeIcon(
        "Lightbulb",
        "M9.663 17h4.673M12 3v1m6.364 1.636l-0.707 0.707M21 12h-1M4 12H3" +
            "m3.343-5.657l-0.707-0.707m2.828 9.9a5 5 0 1 1 7.072 0l-0.548 0.547" +
            "A3.374 3.374 0 0 0 14 18.469V19a2 2 0 1 1-4 0v-0.531" +
            "c0-0.895-0.356-1.754-0.988-2.386l-0.548-0.547z",
    )
}

private const val VIEWPORT = 24f
private const val STROKE_WIDTH = 2f

private fun strokeIcon(name: String, pathData: String): ImageVector =
    ImageVector.Builder(
        name = name,
        defaultWidth = VIEWPORT.dp,
        defaultHeight = VIEWPORT.dp,
        viewportWidth = VIEWPORT,
        viewportHeight = VIEWPORT,
    ).addPath(
        pathData = addPathNodes(pathData),
        stroke = SolidColor(Color.Black),
        strokeLineWidth = STROKE_WIDTH,
        strokeLineCap = StrokeCap.Round,
        strokeLineJoin = StrokeJoin.Round,
    ).build()
