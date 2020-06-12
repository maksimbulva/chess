package ru.maksimbulva.chesslibkt.position

import ru.maksimbulva.chesslibkt.Player
import ru.maksimbulva.chesslibkt.board.Board
import ru.maksimbulva.chesslibkt.board.PieceOnBoard
import ru.maksimbulva.chesslibkt.board.Square
import ru.maksimbulva.chesslibkt.move.Move
import ru.maksimbulva.chesslibkt.move.MoveGenerationFilter
import ru.maksimbulva.chesslibkt.move.ScoredMove

class Position(
    blackKingSquare: Square,
    whiteKingSquare: Square,
    playerToMove: Player,
    enPassantColumn: Int?,
    castleOptions: Map<Player, CastleOptions>,
    halfmoveClock: Int,
    fullmoveNumber: Int
) {

    private var _board = Board(blackKingSquare, whiteKingSquare)
    val board: Board
        get() = _board

    val positionFlags: PositionFlags
        get() = TODO()

    private var _playerToMove = playerToMove
    val playerToMove: Player
        get() = _playerToMove

    val otherPlayer: Player
        get() = TODO()

    private var _enPassantColumn = enPassantColumn
    val enPassantColumn: Int?
        get() = _enPassantColumn

    private var blackCastleOptions = castleOptions.getValue(Player.Black)
    private var whiteCastleOptions = castleOptions.getValue(Player.White)

    private var _halfmoveClock = halfmoveClock
    val halfmoveClock: Int
        get() = _halfmoveClock

    private var _fullmoveNumber = fullmoveNumber
    val fullmoveNumber: Int
        get() = _fullmoveNumber

    fun clone(): Position {
        // TODO()
        return this
    }

    fun generatePseudoLegalMoves(movesFilter: MoveGenerationFilter): List<ScoredMove> {
        // TODO()
        return emptyList()
    }

    fun getCastleOptions(player: Player): CastleOptions {
        return when (player) {
            Player.Black -> blackCastleOptions
            Player.White -> whiteCastleOptions
        }
    }

    fun optimizeCastleOptions() {
//        TODO()
    }

    fun isValid(): Boolean {
//        TODO()
        return true
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
