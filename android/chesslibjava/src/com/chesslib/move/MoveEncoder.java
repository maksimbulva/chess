package com.chesslib.move;

public abstract class MoveEncoder {

    public static int encodePiece(int pieceCode) {
        return pieceCode << 12;
    }

    public static int encodeDestSquare(int destSquare) {
        return destSquare << 6;
    }

    public static int encodePawnPromotion(int promoteToPieceCode) {
        return Move.PROMOTION | (promoteToPieceCode << Move.PROMOTE_TO_PIECE_CODE_SHIFT);
    }

    public static int encodeCapture(int capturedPiece) {
        return Move.CAPTURE | (capturedPiece << Move.CAPTURED_PIECE_SHIFT);
    }
}
