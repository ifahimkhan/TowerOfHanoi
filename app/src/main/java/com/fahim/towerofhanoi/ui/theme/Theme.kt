package com.fahim.towerofhanoi.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.fahim.towerofhanoi.ui.hanoi.HanoiColors

// The design is light-only and token-driven, so Material components (ripples,
// buttons, icon buttons) take their defaults from the same palette.
private val HanoiColorScheme = lightColorScheme(
    primary = HanoiColors.Primary,
    primaryContainer = HanoiColors.PrimaryContainer,
    onPrimaryContainer = HanoiColors.OnPrimaryContainer,
    background = HanoiColors.Surface,
    surface = HanoiColors.Surface,
    surfaceVariant = HanoiColors.SurfaceContainer,
)

@Composable
fun TowerOfHanoiTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = HanoiColorScheme, content = content)
}
