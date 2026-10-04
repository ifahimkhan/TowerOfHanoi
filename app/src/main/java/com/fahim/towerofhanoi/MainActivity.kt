package com.fahim.towerofhanoi

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.fahim.towerofhanoi.ui.TowerOfHanoiApp
import com.fahim.towerofhanoi.ui.theme.TowerOfHanoiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applySystemBars(splashVisible = true)
        setContent {
            TowerOfHanoiTheme {
                TowerOfHanoiApp(onSplashVisibilityChange = ::applySystemBars)
            }
        }
    }

    // Light bar icons over the dark splash; dark icons over the light-only game design.
    private fun applySystemBars(splashVisible: Boolean) {
        val style = if (splashVisible) {
            SystemBarStyle.dark(Color.TRANSPARENT)
        } else {
            SystemBarStyle.light(scrim = Color.TRANSPARENT, darkScrim = Color.TRANSPARENT)
        }
        enableEdgeToEdge(statusBarStyle = style, navigationBarStyle = style)
    }
}
