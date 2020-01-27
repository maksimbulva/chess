package ru.maksimbulva.chess.core.notation

import ru.maksimbulva.chess.core.engine.Piece
import ru.maksimbulva.chess.core.engine.move.Move

object CoordinateNotation {

    fun moveToString(move: Move): String {
        return "${move.fromCell}${move.toCell}${toChar(move.promoteTo)}"
    }

    private fun toChar(piece: Piece?) = when (piece) {
        Piece.Knight -> "n"
        Piece.Bishop -> "b"
        Piece.Rook -> "r"
        Piece.Queen -> "q"
        else -> ""
    }
}
