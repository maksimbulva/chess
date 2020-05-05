package ru.maksimbulva.chess.core.engine.move

import ru.maksimbulva.chess.core.engine.Piece
import ru.maksimbulva.chess.core.engine.Player
import ru.maksimbulva.chess.core.engine.board.Cell

data class DetailedMove(
    val playerToMove: Player,
    val pieceToMove: Piece,
    val fromCell: Cell,
    val toCell: Cell,
    val promoteTo: Piece?,
    val isCapture: Boolean,
    val isCheck: Boolean,
    val isCheckmate: Boolean,
    val isEnPassantCapture: Boolean,
    val isShortCastle: Boolean,
    val isLongCastle: Boolean,
    val fullmoveNumber: Int
) {
    val isCastle: Boolean
        get() = isShortCastle || isLongCastle
}
