package ru.maksimbulva.chesslibkt.position

import ru.maksimbulva.chesslibkt.Player
import ru.maksimbulva.chesslibkt.board.Board
import ru.maksimbulva.chesslibkt.board.PieceOnBoard
import ru.maksimbulva.chesslibkt.board.Square
import ru.maksimbulva.chesslibkt.move.Move
import ru.maksimbulva.chesslibkt.move.MoveGenerationFilter
import ru.maksimbulva.chesslibkt.move.ScoredMove

class Position(
    private var blackKingSquare: Square,
    private var whiteKingSquare: Square,
    playerToMove: Player,
    enPassantColumn: Int?,
    castleOptions: Map<Player, CastleOptions>,
    halfmoveClock: Int,
    fullmoveNumber: Int
) {

    val board: Board
        get() = TODO()

    val positionFlags: PositionFlags
        get() = TODO()

    val playerToMove: Player
        get() = TODO()

    val otherPlayer: Player
        get() = TODO()

    val enPassantColumn: Int?
        get() = TODO()

    val moveCounters: PositionMoveCounters
        get() = TODO()

    init {
        TODO()
    }

    fun clone(): Position {
        TODO()
    }

    fun generatePseudoLegalMoves(movesFilter: MoveGenerationFilter): List<ScoredMove> {
        TODO()
    }

    fun getCastleOptions(player: Player): CastleOptions {
        TODO()
    }

    fun optimizeCastleOptions() {
        TODO()
    }

    fun isValid(): Boolean {
        TODO()
    }

    fun isNotValid(): Boolean = !isValid()

    fun isInCheck(): Boolean {
        TODO()
    }

    fun addPiece(piece: PieceOnBoard) {
        board.addPiece(piece)
    }

    fun playMove(move: Move) {
        TODO()
    }

    fun updateMoveCounters(move: Move) {
        TODO()
    }

    fun isCanUndoMove(): Boolean {
        TODO()
    }

    fun undoMove() {
        TODO()
    }
}
