package ru.maksimbulva.chesslibkt.move

import ru.maksimbulva.chesslibkt.Piece
import ru.maksimbulva.chesslibkt.board.Board
import ru.maksimbulva.chesslibkt.board.Square
import ru.maksimbulva.chesslibkt.encoded

inline class MoveBuilder(private val encoded: Int) {

    private val origin: Square
        get() = Square(encoded and SQUARE_MASK)

    private val dest: Square
        get() = Square((encoded shr 6) and SQUARE_MASK)

    constructor(piece: Piece, origin: Square)
        : this(origin.encoded or (piece.encoded shl 12))

    fun build() = Move(encoded).also { require(!it.isPromotion) }

    fun setDestSquare(dest: Square): MoveBuilder {
        return MoveBuilder(encoded or ((dest.encoded shl 6)))
    }

    fun setPromoteToPieceType(promoteTo: Piece): MoveBuilder {
        return MoveBuilder(encoded or Move.Promotion or (promoteTo.encoded shl 15))
    }

    fun setCapture(board: Board): MoveBuilder {
        val capturedPiece = board.getPieceTypeAt(dest)!!
        return MoveBuilder(encoded or Move.Capture or (capturedPiece.encoded shl 18))
    }

    fun setEnPassantCapture(): MoveBuilder {
        return MoveBuilder(encoded or (Move.Capture or Move.EnPassantCapture) or
                (Piece.Pawn.encoded shl 18))
    }

    fun setPawnDoubleMove(): MoveBuilder {
        return MoveBuilder(encoded or Move.PawnDoubleMove)
    }

    fun setLongCastle(): MoveBuilder {
        return MoveBuilder(encoded or Move.LongCastle)
    }

    fun setShortCastle(): MoveBuilder {
        return MoveBuilder(encoded or Move.ShortCastle)
    }
}
