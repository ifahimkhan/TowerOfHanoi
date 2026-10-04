package com.fahim.towerofhanoi.ui.hanoi

import androidx.compose.ui.graphics.Color

/** Colors from the Stitch "Tower of Hanoi – Material 3 Mobile" design. */
internal object HanoiColors {
    val Surface = Color(0xFFFBF8FD)
    val SurfaceContainer = Color(0xFFF0ECF4)
    val SurfaceContainerHigh = Color(0xFFEAE6EE)
    val Primary = Color(0xFF6750A4)
    val PrimaryContainer = Color(0xFFEADDFF)
    val OnPrimaryContainer = Color(0xFF21005D)

    val Slate50 = Color(0xFFF8FAFC)
    val Slate200 = Color(0xFFE2E8F0)
    val Slate300 = Color(0xFFCBD5E1)
    val Slate400 = Color(0xFF94A3B8)
    val Slate500 = Color(0xFF64748B)
    val Slate600 = Color(0xFF475569)
    val Slate700 = Color(0xFF334155)
    val Slate800 = Color(0xFF1E293B)
    val Slate900 = Color(0xFF0F172A)

    val Purple50 = Color(0xFFFAF5FF)
    val Purple100 = Color(0xFFF3E8FF)
    val Purple200 = Color(0xFFE9D5FF)
    val Purple700 = Color(0xFF7E22CE)

    val Amber50 = Color(0xFFFFFBEB)
    val Amber200 = Color(0xFFFDE68A)
    val Amber500 = Color(0xFFF59E0B)
    val Amber800 = Color(0xFF92400E)

    val RodDark = Color(0xFF71767F)
    val RodLight = Color(0xFFA8ACB4)
    val RodShade = Color(0xFF5E636C)

    val BaseDark = Color(0xFF4A3B32)
    val BaseMid = Color(0xFF635347)
    val BaseShade = Color(0xFF3E3028)
}

internal data class DiskPalette(val fill: Color, val border: Color)

private val DiskPalettes = listOf(
    DiskPalette(fill = Color(0xFFFF5252), border = Color(0xFFD32F2F)), // red coral
    DiskPalette(fill = Color(0xFFFFD600), border = Color(0xFFFBC02D)), // sunny yellow
    DiskPalette(fill = Color(0xFF00E676), border = Color(0xFF00C853)), // emerald
    DiskPalette(fill = Color(0xFF00B0FF), border = Color(0xFF0091EA)), // cyan blue
    DiskPalette(fill = Color(0xFFAA00FF), border = Color(0xFF7B1FA2)), // royal purple
    DiskPalette(fill = Color(0xFFFF6D00), border = Color(0xFFE65100)), // deep orange
    DiskPalette(fill = Color(0xFFFF4081), border = Color(0xFFC2185B)), // hot pink
)

internal fun diskPalette(size: Int): DiskPalette = DiskPalettes[(size - 1).mod(DiskPalettes.size)]
