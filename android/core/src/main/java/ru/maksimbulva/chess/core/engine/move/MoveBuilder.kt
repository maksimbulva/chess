package ru.maksimbulva.chess.core.engine.move

import ru.maksimbulva.chess.core.engine.Piece
import ru.maksimbulva.chess.core.engine.board.Cell

internal data class MoveBuilder(
    private val fromCell: Cell,
    private val toCell: Cell? = null,
    private val promoteTo: Piece? = null,
    private val isEnPassantCapture: Boolean = false,
    private val isCastle: Boolean = false
) {

    fun setToCell(toCell: Cell): MoveBuilder {
        return this.copy(toCell = toCell)
    }

    fun setPromoteTo(piece: Piece): MoveBuilder {
        return this.copy(promoteTo = piece)
    }

    fun setAsEnPassantCapture(): MoveBuilder {
        return this.copy(isEnPassantCapture = true)
    }

    fun setAsCapture(capturedPiece: Piece): MoveBuilder {
        // TODO
        return this
    }

    fun build(): Move {
        return Move(
            fromCell,
            toCell!!,
            promoteTo,
            isEnPassantCapture,
            isCastle
        )
    }
}
