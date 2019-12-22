package ru.maksimbulva.chess.core.pgn

import ru.maksimbulva.chess.core.engine.Piece
import ru.maksimbulva.chess.core.engine.move.Move
import ru.maksimbulva.chess.core.engine.position.Position

sealed class PgnMove {

    abstract fun isMatch(move: Move, position: Position): Boolean

    object PgnShortCastle : PgnMove() {
        override fun isMatch(move: Move, position: Position): Boolean {
            return move.isCastle && move.toCell.column == 6
        }
    }

    object PgnLongCastle : PgnMove() {
        override fun isMatch(move: Move, position: Position): Boolean {
            return move.isCastle && move.toCell.column == 2
        }
    }

    class PgnMovePattern(
        val piece: Piece,
        val fromCell: PgnCellPattern = PgnCellPattern.EMPTY,
        val toCell: PgnCellPattern = PgnCellPattern.EMPTY,
        val promoteTo: Piece? = null
    ) : PgnMove() {

        override fun isMatch(move: Move, position: Position): Boolean {
            if (!fromCell.isMatch(move.fromCell) || !toCell.isMatch(move.toCell)) {
                return false
            }

            val board = position.board
            if (board.pieceAt(move.fromCell)?.piece != piece) {
                return false
            }

            if (promoteTo != null && promoteTo != move.promoteTo) {
                return false
            }

            if (promoteTo == null && move.promoteTo != null) {
                return false
            }

            return true
        }
    }
}