package com.fahim.towerofhanoi

import com.fahim.towerofhanoi.ui.GameState

fun handleDiskSelection(state: GameState, towerIndex: Int, diskSize: Int): GameState {
    // If no disk is selected, select this one if it's the top disk
    if (state.selectedDisk == null) {
        val tower = state.towers[towerIndex]
        if (tower.isNotEmpty() && tower.last() == diskSize) {
            return state.copy(selectedDisk = diskSize)
        }
        return state
    }
    
    // If a disk is already selected, try to move it
    val selectedDisk = state.selectedDisk
    val sourceTower = state.towers.indexOfFirst { it.contains(selectedDisk) }
    
    // Check if move is valid
    val targetTower = state.towers[towerIndex]
    val isValidMove = targetTower.isEmpty() || targetTower.last() > selectedDisk
    
    if (isValidMove && sourceTower != towerIndex) {
        val newTowers = state.towers.toMutableList().map { it.toMutableList() }
        newTowers[sourceTower].removeLast()
        newTowers[towerIndex].add(selectedDisk)
        
        val newState = state.copy(
            towers = newTowers,
            moveCount = state.moveCount + 1,
            selectedDisk = null,
            isComplete = newTowers.last().size == state.diskCount
        )
        
        return newState
    }
    
    // If invalid move or same disk clicked, deselect
    return state.copy(selectedDisk = null)
}