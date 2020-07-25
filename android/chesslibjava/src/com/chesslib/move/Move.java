package com.chesslib.move;

public abstract class Move {
    public static final int SQUARE_MASK = 63;
    public static final int PIECE_TYPE_MASK = 7;

    public static final int PROMOTE_TO_PIECE_CODE_SHIFT = 15;
    public static final int CAPTURED_PIECE_SHIFT = 18;
    public static final int CAPTURE_SHIFT = 21;

    public static final int CAPTURE = 1 << CAPTURE_SHIFT;
    public static final int EN_PASSANT_CAPTURE = 1 << 22;
    public static final int PROMOTION = 1 << 23;
    public static final int SHORT_CASTLE = 1 << 24;
    public static final int LONG_CASTLE = 1 << 25;
    public static final int PAWN_DOUBLE_MOVE = 1 << 26;

    public static final int ANY_CASTLE = SHORT_CASTLE | LONG_CASTLE;

    public static int getOriginSquare(int encodedMove) {
        return encodedMove & SQUARE_MASK;
    }

    public static int getDestSquare(int encodedMove) {
        return (encodedMove >> 6) & SQUARE_MASK;
    }

    public static int getPiece(int encodedMove) {
        return ((encodedMove >> 12) & PIECE_TYPE_MASK);
    }

    public static int getPromoteToPieceType(int encodedMove) {
        return ((encodedMove >> PROMOTE_TO_PIECE_CODE_SHIFT) & PIECE_TYPE_MASK);
    }

    public static int getCapturedPieceType(int encodedMove) {
        return ((encodedMove >> CAPTURED_PIECE_SHIFT) & PIECE_TYPE_MASK);
    }

    public static boolean isCapture(int encodedMove) {
        return (encodedMove & CAPTURE) != 0;
    }

    public static boolean isEnPassantCapture(int encodedMove) {
        return (encodedMove & EN_PASSANT_CAPTURE) != 0;
    }

    public static boolean isPromotion(int encodedMove) {
        return (encodedMove & PROMOTION) != 0;
    }

    public static boolean isShortCastle(int encodedMove) {
        return (encodedMove & SHORT_CASTLE) != 0;
    }

    public static boolean isLongCastle(int encodedMove) {
        return (encodedMove & LONG_CASTLE) != 0;
    }

    public static boolean isCastle(int encodedMove) {
        return (encodedMove & ANY_CASTLE) != 0;
    }

    public static boolean isPawnDoubleMove(int encodedMove) {
        return (encodedMove & PAWN_DOUBLE_MOVE) != 0;
    }
}
