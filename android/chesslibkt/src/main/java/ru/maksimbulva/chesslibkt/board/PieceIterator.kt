package ru.maksimbulva.chesslibkt.board

class PieceIterator(
    private val boardSquares: Array<BoardSquare?>,
    private var currentBoardSquare: BoardSquare?
) : Iterator<BoardSquare> {

    override fun hasNext(): Boolean = currentBoardSquare != null

    override fun next(): BoardSquare {
        val result = currentBoardSquare!!
        currentBoardSquare = result.next
        return result
    }
}
