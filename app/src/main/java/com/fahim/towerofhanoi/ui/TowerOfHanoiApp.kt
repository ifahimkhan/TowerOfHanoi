package com.fahim.towerofhanoi.ui

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.fahim.towerofhanoi.ui.hanoi.HanoiScreen
import com.fahim.towerofhanoi.ui.splash.SplashScreen
import kotlinx.coroutines.delay

const val SPLASH_DURATION_MS = 1_600L
private const val CROSSFADE_MS = 400

/**
 * App entry: shows the Sharingan splash once per launch, then crossfades to the game.
 * The flag is saved, so rotating the device does not replay the splash.
 */
@Composable
fun TowerOfHanoiApp(
    splashDurationMillis: Long = SPLASH_DURATION_MS,
    onSplashVisibilityChange: (Boolean) -> Unit = {},
) {
    var showSplash by rememberSaveable { mutableStateOf(true) }
    val notifyVisibility by rememberUpdatedState(onSplashVisibilityChange)

    LaunchedEffect(showSplash) {
        notifyVisibility(showSplash)
        if (showSplash) {
            delay(splashDurationMillis)
            showSplash = false
        }
    }

    Crossfade(targetState = showSplash, animationSpec = tween(CROSSFADE_MS), label = "splash") { splash ->
        if (splash) SplashScreen() else HanoiScreen()
    }
}
