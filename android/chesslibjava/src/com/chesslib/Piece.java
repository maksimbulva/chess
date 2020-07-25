package com.chesslib;

public enum Piece {
    PAWN(PieceCode.PAWN),
    KNIGHT(PieceCode.KNIGHT),
    BISHOP(PieceCode.BISHOP),
    ROOK(PieceCode.ROOK),
    QUEEN(PieceCode.QUEEN),
    KING(PieceCode.KING);

    public final int pieceCode;

    Piece(int pieceCode)
    {
        this.pieceCode = pieceCode;
    }
}
