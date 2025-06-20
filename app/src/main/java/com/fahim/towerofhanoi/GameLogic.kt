import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect

// GameLogic.kt

fun handleDragStart(state: GameState, towerIndex: Int): GameState {
    val tower = state.towers[towerIndex]
    return if (tower.isNotEmpty()) {
        state.copy(
            dragState = DragState(
                currentTower = towerIndex,
                diskSize = tower.last()
            )
        )
    } else {
        state
    }
}

fun handleDrag(state: GameState, dragOffset: Offset): GameState {
    return state.dragState?.let { dragState ->
        state.copy(
            dragState = dragState.copy(offset = dragOffset)
        )
    } ?: state
}

// GameLogic.kt
fun handleDragEnd(
    state: GameState,
    dropPosition: Offset,
    towerBounds: List<Rect>
): GameState {
    state.dragState ?: return state

    val targetTowerIndex = towerBounds.indexOfFirst { bounds ->
        bounds.contains(dropPosition) || bounds.run {
            val expanded = inflate(width * 0.2f)  // 20% larger bounds for edge cases
            expanded.contains(dropPosition)
        }
    }

    return when {
        targetTowerIndex == -1 -> state.copy(dragState = null)  // Dropped outside

        state.isValidMove(targetTowerIndex) -> {
            val newTowers = state.towers.toMutableList().map { it.toMutableList() }
            newTowers[state.dragState.currentTower].removeAt(newTowers[state.dragState.currentTower].lastIndex)
            newTowers[targetTowerIndex].add(state.dragState.diskSize)

            state.copy(
                towers = newTowers,
                dragState = null,
                moveCount = state.moveCount + 1
            )
        }

        else -> state.copy(dragState = null)  // Invalid move
    }
}