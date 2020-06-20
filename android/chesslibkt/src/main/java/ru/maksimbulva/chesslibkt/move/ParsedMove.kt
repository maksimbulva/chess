package ru.maksimbulva.chesslibkt.move

import ru.maksimbulva.chesslibkt.Piece
import ru.maksimbulva.chesslibkt.board.Square

class ParsedMove(
    val originSquare: Square,
    val destSquare: Square,
    val promoteToPieceType: Piece?
) {

    companion object {
        private const val MOVE_WITHOUT_CAPTURE_STRING_LENGTH = 4
        private const val MOVE_WITH_CAPTURE_STRING_LENGTH = 5

        fun fromCoordinateNotation(moveString: String): ParsedMove {
            val originSquare = parseSquare(moveString.substring(0..1))
            val destSquare = parseSquare(moveString.substring(2..3))
            return when (moveString.length) {
                MOVE_WITHOUT_CAPTURE_STRING_LENGTH ->
                    ParsedMove(originSquare, destSquare, promoteToPieceType = null)
                MOVE_WITH_CAPTURE_STRING_LENGTH ->
                    ParsedMove(originSquare, destSquare, parsePieceType(moveString.last()))
                else -> throw IllegalStateException()
            }
        }

        private fun parseSquare(squareString: String): Square {
            require(squareString.length == 2)
            val columnChar = squareString.first().toLowerCase()
            val rowChar = squareString.last()
            check(columnChar in 'a'..'h' && rowChar in '1'..'8')
            return Square(rowChar - '1', columnChar - 'a')
        }

        private fun parsePieceType(pieceTypeChar: Char): Piece {
            return when (pieceTypeChar.toLowerCase()) {
                'n' -> Piece.Knight
                'b' -> Piece.Bishop
                'r' -> Piece.Rook
                'q' -> Piece.Queen
                'k' -> Piece.King
                else -> throw IllegalStateException()
            }
        }
    }
}
