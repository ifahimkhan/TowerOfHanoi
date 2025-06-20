import android.util.Log
import androidx.compose.ui.geometry.Offset

// GameState.kt
data class GameState(
    val towers: List<List<Int>>,
    val dragState: DragState? = null,
    val moveCount: Int = 0,
    val diskCount: Int = 3
) {
    val isWon: Boolean
        get() = towers.last().size == diskCount

    companion object {
        fun initial(diskCount: Int = 3) = GameState(
            towers = listOf(
                (1..diskCount).toList().reversed(),
                emptyList(),
                emptyList()
            ),
            diskCount = diskCount
        )
    }

    fun isValidMove(targetTowerIndex: Int): Boolean {
        val dragState = this.dragState ?: return false
        val targetTower = towers[targetTowerIndex]
        return targetTower.isEmpty() || dragState.diskSize < targetTower.last()
    }

    fun performMove(targetTowerIndex: Int): GameState {
        Log.e("performMove", "Performing move to $targetTowerIndex")
        if (!isValidMove(targetTowerIndex)) return this

        val newTowers = towers.toMutableList().map { it.toMutableList() }
        newTowers[dragState!!.currentTower].removeAt(newTowers[dragState!!.currentTower].lastIndex)
        newTowers[targetTowerIndex].add(dragState.diskSize)

        return copy(
            towers = newTowers,
            dragState = null,
            moveCount = moveCount + 1
        )
    }
}

data class DragState(
    val currentTower: Int,
    val diskSize: Int,
    val offset: Offset = Offset.Zero
)