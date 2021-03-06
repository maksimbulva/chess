package ru.maksimbulva.chess.core.engine.position

import ru.maksimbulva.chess.core.engine.Piece
import ru.maksimbulva.chess.core.engine.Player
import ru.maksimbulva.chess.core.engine.board.Board
import ru.maksimbulva.chess.core.engine.board.Cell
import ru.maksimbulva.chess.core.engine.move.Move
import ru.maksimbulva.chess.core.engine.move.generator.isAttacksCell
import ru.maksimbulva.chess.core.engine.otherPlayer
import ru.maksimbulva.chess.core.engine.position.history.PositionHistoryItem
import kotlin.math.abs

class Position(
    val board: Board,
    val playerToMove: Player,
    val whiteCastlingAvailability: CastlingAvailability,
    val blackCastlingAvailability: CastlingAvailability,
    val enPassantCaptureColumn: Int?,
    val halfMoveClock: Int,
    val fullMoveNumber: Int,
    private val moveHistory: List<PositionHistoryItem> = emptyList()
) {

    val isCanCaptureEnPassant: Boolean
        get() = enPassantCaptureColumn != null

    val isInCheck: Boolean by lazy {
        isAttacksCell(
            this,
            playerToMove.otherPlayer(),
            board.kingCell(playerToMove)
        )
    }

    fun playMove(move: Move): Position {
        val newEnPassantCaptureColumn: Int? = if (isDoublePawnMove(move)) {
            move.fromCell.column
        } else {
            null
        }
        val newWhiteCastlingAvailability = if (playerToMove == Player.White) {
            updateCastlingAvailability(whiteCastlingAvailability, move)
        } else {
            whiteCastlingAvailability
        }
        val newBlackCastlingAvailability = if (playerToMove == Player.Black) {
            updateCastlingAvailability(blackCastlingAvailability, move)
        } else {
            blackCastlingAvailability
        }

        val newMoveHistory = moveHistory.toMutableList().apply {
            add(
                PositionHistoryItem(
                movePlayed = move,
                whiteCastlingAvailability = whiteCastlingAvailability,
                blackCastlingAvailability = blackCastlingAvailability,
                enPassantCaptureColumn = enPassantCaptureColumn,
                halfMoveClock = halfMoveClock,
                fullMoveNumber = fullMoveNumber
            ))
        }

        return Position(
            board.playMove(move, playerToMove),
            playerToMove.otherPlayer(),
            newWhiteCastlingAvailability,
            newBlackCastlingAvailability,
            newEnPassantCaptureColumn,
            updateHalfmoveClock(halfMoveClock, move),
            fullMoveNumber + if (playerToMove == Player.Black) 1 else 0,
            newMoveHistory
        )
    }

    fun castlingAvailability(player: Player): CastlingAvailability {
        return when (player) {
            Player.Black -> blackCastlingAvailability
            Player.White -> whiteCastlingAvailability
        }
    }

    private fun updateCastlingAvailability(
        currentValue: CastlingAvailability,
        move: Move
    ): CastlingAvailability {

        val movedPiece = board.pieceAt(move.fromCell)!!.piece
        return when (movedPiece) {
            Piece.King -> CastlingAvailability(false, false)
            Piece.Rook -> {
                val baseRow = if (playerToMove == Player.Black) 7 else 0
                val canCastleShort = currentValue.canCastleShort &&
                        move.fromCell != Cell(baseRow, 7)
                val canCastleLong = currentValue.canCastleLong &&
                        move.fromCell != Cell(baseRow, 0)
                CastlingAvailability(canCastleShort, canCastleLong)
            }
            else -> currentValue
        }
    }

    private fun isDoublePawnMove(move: Move): Boolean {
        return board.pieceAt(move.fromCell)?.piece == Piece.Pawn
                && abs(move.fromCell.row - move.toCell.row) == 2
    }

    private fun updateHalfmoveClock(currentValue: Int, move: Move): Int {
        return if (board.pieceAt(move.fromCell)?.piece == Piece.Pawn ||
            board.pieceAt(move.toCell)?.player == playerToMove.otherPlayer()
        ) {
            0
        } else {
            currentValue + 1
        }
    }
}
