package ru.maksimbulva.chesslibkt

enum class Piece {
    Pawn,
    Knight,
    Bishop,
    Rook,
    Queen,
    King
}

object PieceEncoded {
    const val Pawn = 1
    const val Knight = 2
    const val Bishop = 3
    const val Rook = 4
    const val Queen = 5
    const val King = 6
}

private val encodedToPiece = arrayOf(
    null,
    Piece.Pawn,
    Piece.Knight,
    Piece.Bishop,
    Piece.Rook,
    Piece.Queen,
    Piece.King
)

private val nonNullEncodedToPiece = arrayOf(
    Piece.Pawn,  // this value should be never accessed as encoded value 0 is reserved for null
    Piece.Pawn,
    Piece.Knight,
    Piece.Bishop,
    Piece.Rook,
    Piece.Queen,
    Piece.King
)

val Piece.encoded: Int
    get() = ordinal + 1

fun pieceFromEncoded(encoded: Int): Piece? = encodedToPiece[encoded]
