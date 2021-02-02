package ru.maksimbulva.chess.core.engine.move

import ru.maksimbulva.chess.core.engine.Piece
import ru.maksimbulva.chess.core.engine.board.Cell

internal data class MoveBuilder(
    private val fromCell: Cell,
    private val toCell: Cell? = null,
    private val promoteTo: Piece? = null,
    private val isEnPassantCapture: Boolean = false,
    private val capturedPiece: Piece? = null,
    private val isCastle: Boolean = false
) {

    fun setToCell(toCell: Cell): MoveBuilder {
        return this.copy(toCell = toCell)
    }

    fun setPromoteTo(piece: Piece): MoveBuilder {
        return this.copy(promoteTo = piece)
    }

    fun setAsEnPassantCapture(): MoveBuilder {
        return this.copy(capturedPiece = Piece.Pawn, isEnPassantCapture = true)
    }

    fun setAsCapture(capturedPiece: Piece): MoveBuilder {
        return this.copy(capturedPiece = capturedPiece)
    }

    fun build(): Move {
        return Move(
            fromCell,
            toCell!!,
            promoteTo,
            isEnPassantCapture,
            capturedPiece,
            isCastle
        )
    }
}
