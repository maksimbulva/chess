package com.chesslib.board;

import com.chesslib.Piece;
import com.chesslib.PieceCode;

public final class PieceBitboards {
    private long allPieces;
    private final long[] pieces = new long[PieceCode.KING + 1];

    public long getAllPieces() {
        return allPieces;
    }

    public void addPiece(Piece piece, int boardSquare) {
        final long bitToSet = 1L << boardSquare;
        allPieces = allPieces | bitToSet;
        pieces[piece.pieceCode] = pieces[piece.pieceCode] | bitToSet;
    }

    public void movePiece(int piece, int oldBoardSquare, int newBoardSquare) {
        pieces[piece] = Bitboard.moveBit(pieces[piece], oldBoardSquare, newBoardSquare);
    }
}
