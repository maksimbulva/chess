package chess.engine.fen

import chess.engine.core.ColoredPiece
import chess.engine.core.Piece
import chess.engine.core.Player
import chess.engine.core.board.Board
import chess.engine.core.board.Cell
import chess.engine.core.position.CastlingAvailability
import chess.engine.core.position.Position
import extensions.toUpperCaseIf

object FenDecoder {

    // Decodes chess positions into Forsyth–Edwards notation format
    fun decode(encoded: String): Position {
        val splited = encoded.split(' ')
        val board = decodeBoard(splited[0])
        val playerToMove = decodePlayerToMove(splited[1])
        val whiteCastlingAvailability = decodeCastlingAvailability(splited[2], Player.White)
        val blackCastlingAvailability = decodeCastlingAvailability(splited[2], Player.Black)
        val enPassantCaptureColumn = decodeEnPassantCaptureAvailability(splited[3])
        val halfMoveClock = if (splited.size > 4) splited[4].toInt() else 0
        val fullMoveNumber = if (splited.size > 5) splited[5].toInt() else 0

        return Position(
            board,
            playerToMove,
            whiteCastlingAvailability,
            blackCastlingAvailability,
            enPassantCaptureColumn,
            halfMoveClock,
            fullMoveNumber
        )
    }

    private fun decodeBoard(encoded: String): Board {
        val encodedRows = encoded.split(FenFormat.rowSeparator)
        require(encodedRows.size == Board.ROW_COUNT)

        val piecesOnBoard = encodedRows.reversed().flatMap { rowString ->
            rowString.flatMap { char ->
                if (char.isDigit()) {
                    Array<ColoredPiece?>(size = char - '0') { null }.asIterable()
                } else {
                    listOf<ColoredPiece?>(decodePiece(char))
                }
            }
        }

        return Board(piecesOnBoard.toTypedArray())
    }

    private fun decodePlayerToMove(encoded: String): Player {
        return when (encoded.toLowerCase()) {
            FenFormat.blackToMove -> Player.Black
            FenFormat.whiteToMove -> Player.White
            else -> throw Exception()
        }
    }

    private fun decodeCastlingAvailability(encoded: String, player: Player): CastlingAvailability {
        val canCastleShortChar = FenFormat.canCastleShort.toUpperCaseIf { player == Player.White }
        val canCastleLongChar = FenFormat.canCastleLong.toUpperCaseIf { player == Player.White }
        return CastlingAvailability(
            canCastleShort = canCastleShortChar in encoded,
            canCastleLong = canCastleLongChar in encoded
        )
    }

    private fun decodeEnPassantCaptureAvailability(encoded: String): Int? {
        return if (encoded == FenFormat.cannotCaptureEnPassant) {
            null
        } else {
            Cell.of(encoded).column
        }
    }

    private fun decodePiece(char: Char): ColoredPiece {
        val player = if (char.isUpperCase()) Player.White else Player.Black
        val piece = when (char.toLowerCase()) {
            'p' -> Piece.Pawn
            'n' -> Piece.Knight
            'b' -> Piece.Bishop
            'r' -> Piece.Rook
            'q' -> Piece.Queen
            'k' -> Piece.King
            else -> throw Exception()
        }
        return ColoredPiece(player, piece)
    }
}