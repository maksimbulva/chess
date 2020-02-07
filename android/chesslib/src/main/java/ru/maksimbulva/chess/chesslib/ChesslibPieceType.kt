package ru.maksimbulva.chess.chesslib

import ru.maksimbulva.chess.core.engine.Piece

private const val CHESSLIB_PAWN = 1
private const val CHESSLIB_KNIGHT = 2
private const val CHESSLIB_BISHOP = 3
private const val CHESSLIB_ROOK = 4
private const val CHESSLIB_QUEEN = 5
private const val CHESSLIB_KING = 6

fun Piece.toChesslibPieceType() = when (this) {
    Piece.Pawn -> CHESSLIB_PAWN
    Piece.Knight -> CHESSLIB_KNIGHT
    Piece.Bishop -> CHESSLIB_BISHOP
    Piece.Rook -> CHESSLIB_ROOK
    Piece.Queen -> CHESSLIB_QUEEN
    Piece.King -> CHESSLIB_KING
}
