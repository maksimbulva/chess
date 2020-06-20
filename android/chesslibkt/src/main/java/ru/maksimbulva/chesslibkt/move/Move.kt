package ru.maksimbulva.chesslibkt.move

import ru.maksimbulva.chesslibkt.Piece
import ru.maksimbulva.chesslibkt.PieceEncoded
import ru.maksimbulva.chesslibkt.board.Square
import ru.maksimbulva.chesslibkt.pieceFromEncoded

val NullMove = Move(0)

// Use MoveBuilder class to get encoded value
inline class Move(
    /**
     * bits 0..5 - move origin square
     * bits 6..11 - move destination square
     * bits 12..14 - piece type to move
     * bits 15..17 - promote to piece type (pawn promotion)
     * bits 18..20 - captured piece type (for move undos)
     */
    private val encoded: Int
) {

    val originSquare: Square
        get() = Square(encoded and SQUARE_MASK)

    val destSquare: Square
        get() = Square((encoded shr 6) and SQUARE_MASK)

    private val pieceEncoded: Int
        get() = (encoded shr 12) and PIECE_TYPE_MASK

    val isPawnMove: Boolean
        get() = pieceEncoded == PieceEncoded.Pawn

//    piece_type_t getPieceType() const
//    {
//        return ;
//    }

//    square_t getCapturedPieceSquare() const
//    {
//        if (isEnPassantCapture()) {
//            // TODO: possibly this can be optimized
//            return encodeSquare(getRow(getOriginSquare()), getColumn(getDestSquare()));
//        }
//        else {
//            return getDestSquare();
//        }
//    }

    val promoteToPiece: Piece
        get() = when ((encoded shr 15) and PIECE_TYPE_MASK) {
            PieceEncoded.Knight -> Piece.Knight
            PieceEncoded.Bishop -> Piece.Bishop
            PieceEncoded.Rook -> Piece.Rook
            else -> Piece.Queen
        }

    val capturedPiece: Piece
        get() = when ((encoded shl 18) and PIECE_TYPE_MASK) {
            PieceEncoded.Pawn -> Piece.Pawn
            PieceEncoded.Knight -> Piece.Knight
            PieceEncoded.Bishop -> Piece.Bishop
            PieceEncoded.Rook -> Piece.Rook
            else -> Piece.Queen
        }

    val isCapture: Boolean
        get() = (encoded and Capture) != 0

    val isEnPassantCapture: Boolean
        get() = (encoded and EnPassantCapture) != 0

    val isPromotion: Boolean
        get() = (encoded and Promotion) != 0

    val isShortCastle: Boolean
        get() = (encoded and ShortCastle) != 0

    val isLongCastle: Boolean
        get() = (encoded and LongCastle) != 0

    val isCastle: Boolean
        get() = (encoded and AnyCastle) != 0

    val isPawnDoubleMove: Boolean
        get() = (encoded and PawnDoubleMove) != 0

    val isNullMove: Boolean
        get() = this == NullMove

    val getEncodedValue: Int
        get() = encoded

    companion object {
        const val Capture = 1 shl 21
        const val EnPassantCapture = 1 shl 22
        const val Promotion = 1 shl 23
        const val ShortCastle = 1 shl 24
        const val LongCastle = 1 shl 25
        const val PawnDoubleMove = 1 shl 26

        private const val AnyCastle = ShortCastle or LongCastle
    }
}

const val SQUARE_MASK = 63
const val PIECE_TYPE_MASK = 7
