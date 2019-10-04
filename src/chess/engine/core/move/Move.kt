package chess.engine.core.move

import chess.engine.core.Piece
import chess.engine.core.Player
import chess.engine.core.board.Cell

class Move(
    val fromCell: Cell,
    val toCell: Cell,
    val promoteTo: Piece? = null,
    val isEnPassantCapture: Boolean = false,
    val isCastle: Boolean = false
) {
    constructor(fromCell: Cell, toCell: Cell)
            : this(fromCell, toCell, isEnPassantCapture = false)

    companion object {
        fun createShortCastle(player: Player): Move {
            return when (player) {
                Player.Black -> Move(Cell.of("e8"), Cell.of("g8"), isCastle = true)
                Player.White -> Move(Cell.of("e1"), Cell.of("g1"), isCastle = true)
            }
        }

        fun createLongCastle(player: Player): Move {
            return when (player) {
                Player.Black -> Move(Cell.of("e8"), Cell.of("c8"), isCastle = true)
                Player.White -> Move(Cell.of("e1"), Cell.of("c1"), isCastle = true)
            }
        }
    }
}
