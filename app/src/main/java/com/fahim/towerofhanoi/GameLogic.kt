fun resetGame(numDisks: Int): GameState {
    return GameState(
        pegs = listOf(
            (1..numDisks).toList().reversed(),
            emptyList(),
            emptyList()
        ),
        numDisks = numDisks
    )
}

fun moveDisk(state: GameState, fromPeg: Int, toPeg: Int): GameState {
    if (fromPeg == toPeg || state.isWon) return state

    val sourcePeg = state.pegs[fromPeg]
    if (sourcePeg.isEmpty()) return state

    val destPeg = state.pegs[toPeg]
    val diskToMove = sourcePeg.last()

    return if (destPeg.isEmpty() || diskToMove < destPeg.last()) {
        val newPegs = state.pegs.map { it.toMutableList() }
        newPegs[fromPeg].removeAt(newPegs[fromPeg].lastIndex)
        newPegs[toPeg].add(diskToMove)

        val isWon = newPegs[1].size == state.numDisks || newPegs[2].size == state.numDisks

        state.copy(
            pegs = newPegs,
            moves = state.moves + 1,
            isWon = isWon,
            selectedPeg = null
        )
    } else {
        state // Invalid move
    }
}

fun handlePegClick(state: GameState, pegIndex: Int): GameState {
    if (state.isWon || state.draggedDisk != null) return state

    return if (state.selectedPeg == null) {
        if (state.pegs[pegIndex].isNotEmpty()) {
            state.copy(selectedPeg = pegIndex)
        } else {
            state
        }
    } else {
        moveDisk(state, state.selectedPeg, pegIndex)
    }
}

fun handleDrop(state: GameState, toPeg: Int): GameState {
    val draggedDisk = state.draggedDisk ?: return state
    return moveDisk(state, draggedDisk.fromPeg, toPeg).copy(
        draggedDisk = null
    )
}