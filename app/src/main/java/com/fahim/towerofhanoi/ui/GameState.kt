data class GameState(
    val pegs: List<List<Int>> = listOf(
        (1..3).toList().reversed(),  // Tower 0 (left)
        emptyList(),                  // Tower 1 (middle)
        emptyList()                   // Tower 2 (right)
    ),
    val selectedPeg: Int? = null,
    val moves: Int = 0,
    val numDisks: Int = 3,
    val isWon: Boolean = false,
    val draggedDisk: DraggedDisk? = null,
    val hint: String? = null
) {
    val optimalMoves: Int
        get() = (1 shl numDisks) - 1  // 2^n - 1

    data class DraggedDisk(
        val diskSize: Int,
        val fromPeg: Int
    )
}