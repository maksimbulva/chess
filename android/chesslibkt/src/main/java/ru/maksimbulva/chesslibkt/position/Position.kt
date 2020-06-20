package ru.maksimbulva.chesslibkt.position

import ru.maksimbulva.chesslibkt.Player
import ru.maksimbulva.chesslibkt.board.*
import ru.maksimbulva.chesslibkt.getOtherPlayer
import ru.maksimbulva.chesslibkt.move.Move
import ru.maksimbulva.chesslibkt.move.MoveGenerationFilter
import ru.maksimbulva.chesslibkt.move.MoveGenerator
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

    private var _positionFlags = PositionFlags(0)
    val positionFlags: PositionFlags
        get() = _positionFlags

    private var _playerToMove = playerToMove
    val playerToMove: Player
        get() = _playerToMove

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

    // TODO: consider another type of container if needed
    private val history = mutableListOf<PositionHistory>()

    fun clone(): Position {
        // TODO()
        return this
    }

    fun generatePseudoLegalMoves(movesFilter: MoveGenerationFilter): List<Move> {
        val movesCollection = mutableListOf<Move>()
        MoveGenerator.fillWithPseudoLegalMoves(movesCollection, movesFilter, this)
        return movesCollection
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
        val playerToMove = playerToMove
        val otherPlayer = getOtherPlayer(playerToMove)

        val originSquare = move.originSquare
        val originRow = originSquare.row
        val destSquare = move.destSquare
//        piece_type_t capturedPieceType = NoPiece;

        history.add(PositionHistory(
            move,
            _enPassantColumn,
            _positionFlags,
            _halfmoveClock,
            _fullmoveNumber
        ))

        // TODO
        if (move.isCapture) {
            TODO()
//            capturedPieceType = move.getCapturedPieceType();
            // TODO: optimization - there is no need to clear the dest square first
            // if it is not an en passant capture
//            _board.erasePieceAt(move.getCapturedPieceSquare())
//            _board.updatePieceSquare(originSquare, destSquare)
        } else {
            _board.updatePieceSquare(originSquare, destSquare)
            if (move.isLongCastle) {
                TODO()
//                square_t rookOriginSquare = encodeSquare(originRow, COLUMN_A)
//                square_t rookDestSquare = encodeSquare(originRow, COLUMN_D)
//                board_.updatePieceSquare(rookOriginSquare, rookDestSquare)
            } else if (move.isShortCastle) {
                TODO()
//                square_t rookOriginSquare = encodeSquare(originRow, COLUMN_H)
//                square_t rookDestSquare = encodeSquare(originRow, COLUMN_F)
//                board_.updatePieceSquare(rookOriginSquare, rookDestSquare)
            }
        }

        if (move.isPromotion) {
            TODO()
//            board_.promotePawn(destSquare, move.promoteToPiece)
        }

        _positionFlags = _positionFlags.onMovePlayed()

//        val castleOptions = getCastleOptions(playerToMove)
//        const piece_type_t pieceToMove = move.getPieceType();
//        if (pieceToMove == King) {
//            castleOptions = CastleOptions();
//        }
//        else if (pieceToMove == Rook) {
//            if (castleOptions.isCanCastleLong() && getColumn(originSquare) == COLUMN_A) {
//                castleOptions.setCanCastleLong(false);
//            }
//            else if (castleOptions.isCanCastleShort() && getColumn(originSquare) == COLUMN_H) {
//                castleOptions.setCanCastleShort(false);
//            }
//        }
//        positionFlags_.setCastleOptions(playerToMove, castleOptions);

//        if (capturedPieceType == Rook) {
//            // TODO: Optimize me
//            const square_t otherBaseRow = playerToMove == Black ? 0 : MAX_ROW;
//            const player_t otherPlayer = getOtherPlayer();
//            CastleOptions otherCastleOptions = getCastleOptions(otherPlayer);
//            if (otherCastleOptions.isCanCastleLong() && destSquare == encodeSquare(otherBaseRow, COLUMN_A)) {
//                otherCastleOptions.setCanCastleLong(false);
//            }
//            else if (otherCastleOptions.isCanCastleShort() && destSquare == encodeSquare(otherBaseRow, COLUMN_H)) {
//                otherCastleOptions.setCanCastleShort(false);
//            }
//            positionFlags_.setCastleOptions(otherPlayer, otherCastleOptions);
//        }

        _enPassantColumn = if (move.isPawnDoubleMove) {
            originSquare.column
        } else {
            null
        }
//        positionFlags_.setEnPassantColumn(
//            move.isPawnDoubleMove() ? OptionalColumn::fromColumn(getColumn(originSquare)) : OptionalColumn());

        _playerToMove = getOtherPlayer(_playerToMove)
        _positionFlags = _positionFlags.setPlayerToMove(otherPlayer)
    }

    fun updateMoveCounters(move: Move) {
        _halfmoveClock = if (move.isCapture || move.isPawnMove) {
            0
        } else {
            _halfmoveClock + 1
        }

        if (_playerToMove == Player.White) {
            ++_fullmoveNumber
        }
    }

    fun isCanUndoMove(): Boolean {
        TODO()
    }

    fun undoMove() {
        require(history.isNotEmpty())

        val historyToUndo = history.last()
        history.removeAt(history.lastIndex)

        undoMove(historyToUndo.move)

        _playerToMove = getOtherPlayer(_playerToMove)
        _enPassantColumn = historyToUndo.enPassantColumn
        _positionFlags = historyToUndo.positionFlags
//        _moveCounters = historyToUndo.getPositionMoveCounters();
    }

    private fun undoMove(move: Move) {
        val originSquare = move.originSquare
//        const square_t originRow = getRow(originSquare);
//        const square_t destSquare = move.getDestSquare();

        // TODO
        if (move.isCapture) {
            TODO()
//            // TODO
//            board_.updatePieceSquare(destSquare, originSquare);
//
//            const square_t capturedPieceSquare = move.isEnPassantCapture()
//            ? encodeSquare(originRow, getColumn(destSquare))
//            : destSquare;
//            board_.addPiece({ playerToMove, move.getCapturedPieceType(), capturedPieceSquare });
        } else {
            _board.updatePieceSquare(move.destSquare, originSquare)
            if (move.isCastle) {
                undoCastleRookMovement(move)
            }
        }

        if (move.isPromotion) {
            TODO()
//            board_.demoteToPawn(originSquare);
        }
    }

    private fun undoCastleRookMovement(castleMove: Move) {
        val originRow = castleMove.originSquare.row
        if (castleMove.isLongCastle) {
            val rookOriginSquare = Square(originRow, Board.COLUMN_A)
            val rookDestSquare = Square(originRow, Board.COLUMN_D)
            _board.updatePieceSquare(rookDestSquare, rookOriginSquare)
        } else {
            val rookOriginSquare = Square(originRow, Board.COLUMN_H)
            val rookDestSquare = Square(originRow, Board.COLUMN_F)
            _board.updatePieceSquare(rookDestSquare, rookOriginSquare);
        }
    }
}
