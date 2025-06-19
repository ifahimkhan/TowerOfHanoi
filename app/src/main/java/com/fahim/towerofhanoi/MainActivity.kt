package com.fahim.towerofhanoi

import ads_mobile_sdk.h4
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fahim.towerofhanoi.ui.GameControls
import com.fahim.towerofhanoi.ui.GameState
import com.fahim.towerofhanoi.ui.GameWonDialog
import com.fahim.towerofhanoi.ui.LevelSelectionDialog
import com.fahim.towerofhanoi.ui.TowersView
import com.fahim.towerofhanoi.ui.theme.TowerOfHanoiTheme
// MainActivity.kt
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TowerOfHanoiTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    TowerOfHanoiGame()
                }
            }
        }
    }
}

@Composable
fun TowerOfHanoiGame() {
    var gameState by remember { mutableStateOf(GameState()) }
    var showLevelSelection by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            GameInfo(gameState)

            TowersView(
                towers = gameState.towers,
                selectedDisk = gameState.selectedDisk,
                onDiskSelected = { towerIndex, diskSize ->
                    gameState = handleDiskSelection(gameState, towerIndex, diskSize)
                }
            )

            GameControls(
                onReset = { gameState = GameState() },
                onUndo = { /* Implement undo logic */ },
                onLevelSelect = { showLevelSelection = true }
            )
        }

        if (gameState.isWon()) {
            GameWonDialog(
                moveCount = gameState.moveCount,
                onDismiss = { gameState = GameState() }
            )
        }

        if (showLevelSelection) {
            LevelSelectionDialog(
                onLevelSelected = { diskCount ->
                    gameState = GameState(
                        towers = listOf(
                            (1..diskCount).toList().reversed(),
                            emptyList(),
                            emptyList()
                        )
                    )
                    showLevelSelection = false
                },
                onDismiss = { showLevelSelection = false }
            )
        }
    }
}

@Composable
fun GameInfo(gameState: GameState) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Tower of Hanoi",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(16.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(text = "Moves: ${gameState.moveCount}")
            Text(text = "Disks: ${gameState.diskCount}")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true,)
@Composable
private fun preview_TowerOfHanoi() {
    TowerOfHanoiGame()
}