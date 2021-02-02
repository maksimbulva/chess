package ru.maksimbulva.chess.core.engine.move

import ru.maksimbulva.chess.core.engine.Piece
import ru.maksimbulva.chess.core.engine.board.Cell

data class Move(
    val fromCell: Cell,
    val toCell: Cell,
    val promoteTo: Piece?,
    val isEnPassantCapture: Boolean,
    val capturedPiece: Piece?,
    val isCastle: Boolean
) {
    val isCapture: Boolean
        get() = capturedPiece != null

    val isPawnPromotion: Boolean
        get() = promoteTo != null
}
