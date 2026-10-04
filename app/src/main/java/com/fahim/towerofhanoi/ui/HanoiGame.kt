package com.fahim.towerofhanoi.ui

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.pow

@Composable
fun HanoiGame() {
    var numDisks by remember { mutableStateOf(3) }
    var pegs by remember {
        mutableStateOf(
            listOf(
                (numDisks downTo 1).toList().reversed(),
                emptyList(),
                emptyList()
            )
        )
    }
    var moves by remember { mutableStateOf(0) }
    var selectedPeg by remember { mutableStateOf<Int?>(null) }
    var isWon by remember { mutableStateOf(false) }

    val optimalMoves = 2.0.pow(numDisks).toInt() - 1

    fun resetGame() {
        pegs = listOf(
            (numDisks downTo 1).toList(),
            emptyList(),
            emptyList()
        )
        moves = 0
        selectedPeg = null
        isWon = false
    }

    fun moveDisk(from: Int, to: Int) {
        if (from == to || isWon) return
        val newPegs = pegs.map { it.toMutableList() }

        if (newPegs[from].isEmpty()) return

        val disk = newPegs[from].last()
        val canMove = newPegs[to].isEmpty() || disk < newPegs[to].last()
        if (!canMove) return

        newPegs[from].removeAt(newPegs[from].lastIndex)
        newPegs[to].add(disk)

        pegs = newPegs
        moves++

        if (pegs[1].size == numDisks || pegs[2].size == numDisks) {
            isWon = true
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Moves: $moves / Optimal: $optimalMoves", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .horizontalScroll(rememberScrollState()) // Makes the row scrollable
        ) {
            (3..8).forEach {
                Button(
                    onClick = {
                        numDisks = it
                        resetGame()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (numDisks == it) Color.Black else Color.LightGray
                    )
                ) {
                    Text("$it Disks")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            for (i in 0..2) {
                Peg(
                    index = i,
                    disks = pegs[i],
                    selectedPeg = selectedPeg,
                    onSelect = {
                        if (selectedPeg == null && pegs[i].isNotEmpty()) {
                            selectedPeg = i
                        } else {
                            moveDisk(selectedPeg ?: i, i)
                            selectedPeg = null
                        }
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { resetGame() },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black
            )
        ) {
            Text("Reset")
        }

        if (isWon) {
            AlertDialog(
                onDismissRequest = { isWon = false },
                confirmButton = {
                    Button(onClick = {
                        isWon = false
                        resetGame()
                    }) {
                        Text("Play Again")
                    }
                },
                title = { Text("You Won!") },
                text = { Text("Completed in $moves moves. Optimal: $optimalMoves") }
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun preview_TowerOfHanoi() {
    HanoiGame()
}