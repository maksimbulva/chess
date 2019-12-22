package ru.maksimbulva.chess.core.engine.fen

import ru.maksimbulva.chess.core.engine.ColoredPiece
import ru.maksimbulva.chess.core.engine.Piece
import ru.maksimbulva.chess.core.engine.Player
import ru.maksimbulva.chess.core.engine.board.Board
import ru.maksimbulva.chess.core.engine.columnToString
import ru.maksimbulva.chess.core.engine.position.CastlingAvailability
import ru.maksimbulva.chess.core.engine.position.Position
import ru.maksimbulva.chess.core.extensions.toUpperCaseIf

object FenEncoder {

    // Encodes chess positions into Forsyth–Edwards notation format
    fun encode(position: Position, options: EncodingOptions): String {
        return arrayOf(
            encodeBoard(position.board),
            encodePlayerToMove(position.playerToMove),
            encodeCastlingAvailability(position),
            encodeEnPassantCaptureAvailability(position),
            position.halfMoveClock.toString(),
            encodeFullMoveNumber(position.fullMoveNumber, options)
        )
            .joinToString(separator = " ")
    }

    private fun encodeBoard(board: Board): String {
        return (0 until Board.ROW_COUNT).reversed().map { row ->
            encodeRow(board.rowSequence(row))
        }
            .joinToString(FenFormat.rowSeparator)
    }

    private fun encodeRow(rowSequence: Sequence<ColoredPiece?>): String {
        val sb = StringBuilder()

        var emptyCells = 0
        var column = 0
        rowSequence.forEach { piece ->
            if (piece == null) {
                emptyCells += 1
            } else {
                if (emptyCells != 0) {
                    sb.append(emptyCells)
                    emptyCells = 0
                }
                sb.append(encodePiece(piece))
            }
            column += 1
        }

        if (emptyCells != 0) {
            sb.append(emptyCells)
        }

        return sb.toString()
    }

    private fun encodePlayerToMove(playerToMove: Player) = when (playerToMove) {
        Player.Black -> FenFormat.blackToMove
        Player.White -> FenFormat.whiteToMove
    }

    private fun encodeCastlingAvailability(position: Position): String {
        val result = encodeCastlingAvailability(position.whiteCastlingAvailability, Player.White) +
                encodeCastlingAvailability(position.blackCastlingAvailability, Player.Black)
        return if (result.isNotEmpty()) result else FenFormat.noOneCanCastle
    }

    private fun encodeCastlingAvailability(caslingAvailability: CastlingAvailability, player: Player): String {
        var result = ""
        if (caslingAvailability.canCastleShort) {
            result += FenFormat.canCastleShort
        }
        if (caslingAvailability.canCastleLong) {
            result += FenFormat.canCastleLong
        }
        return result.toUpperCaseIf { player == Player.White }
    }

    private fun encodeEnPassantCaptureAvailability(position: Position): String {
        val column = position.enPassantCaptureColumn ?: return FenFormat.cannotCaptureEnPassant
        val rowString = if (position.playerToMove == Player.Black) {
            FenFormat.blackEnPassantCaptureRow
        } else {
            FenFormat.whiteEnPassantCaptureRow
        }
        return columnToString(column) + rowString
    }

    private fun encodePiece(piece: ColoredPiece): Char {
        val pieceChar = when (piece.piece) {
            Piece.Pawn -> 'p'
            Piece.Knight -> 'n'
            Piece.Bishop -> 'b'
            Piece.Rook -> 'r'
            Piece.Queen -> 'q'
            Piece.King -> 'k'
        }
        return when (piece.player) {
            Player.Black -> pieceChar
            Player.White -> pieceChar.toUpperCase()
        }
    }

    private fun encodeFullMoveNumber(fullMoveCounter: Int, options: EncodingOptions): String {
        return when (options) {
            EncodingOptions.None -> fullMoveCounter.toString()
            EncodingOptions.SetMovesCountToOne -> "1"
        }
    }

    enum class EncodingOptions {
        None,
        // Will write zero as a halfmove timer value
        // Use this option to produce the same output as some other engines
        SetMovesCountToOne,
    }
}