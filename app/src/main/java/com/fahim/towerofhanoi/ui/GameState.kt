package com.fahim.towerofhanoi.ui

// GameState.kt
data class GameState(
    val towers: List<List<Int>> = listOf(
        (1..3).toList().reversed(),
        emptyList(),
        emptyList()
    ),
    val moveCount: Int = 0,
    val isComplete: Boolean = false,
    val selectedDisk: Int? = null
) {
    val diskCount = towers.flatten().distinct().count()
    
    fun isWon(): Boolean = towers.last().size == diskCount
}