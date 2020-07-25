package com.chesslib.board;

public final class Bitboard {

    private static final int[] LEAST_SIGNIFICANT_BIT = new int[0x100];

    public static final long ROW1 = createRowBitboard(0);
    public static final long ROW2 = createRowBitboard(1);
    public static final long ROW7 = createRowBitboard(6);
    public static final long ROW8 = createRowBitboard(7);

    public static final long COLUMN_A = createColumnBitboard(0);
    public static final long COLUMN_B = createColumnBitboard(1);
    public static final long COLUMN_G = createColumnBitboard(6);
    public static final long COLUMN_H = createColumnBitboard(7);

    public static final long NOT_COLUMN_A = ~COLUMN_A;
    public static final long NOT_COLUMN_B = ~COLUMN_B;
    public static final long NOT_COLUMN_G = ~COLUMN_G;
    public static final long NOT_COLUMN_H = ~COLUMN_H;

    public static final long NOT_COLUMN_AB = NOT_COLUMN_A & NOT_COLUMN_B;
    public static final long NOT_COLUMN_GH = NOT_COLUMN_G & NOT_COLUMN_H;

    static {
        for (int i = 0; i < LEAST_SIGNIFICANT_BIT.length; ++i) {
            LEAST_SIGNIFICANT_BIT[i] = findLeastSignificantBit(i);
        }
    }

    public static int getLeastSignificantBit(int value) {
        return LEAST_SIGNIFICANT_BIT[value];
    }

    private static int findLeastSignificantBit(int value) {
        for (int bitPosition = 0; bitPosition < 32; ++bitPosition) {
            final int bit = 1 << bitPosition;
            if ((value & bit) != 0) {
                return bitPosition;
            }
        }
        return 0;
    }

    public static long setBit(long bitboard, int bitPosition) {
        return bitboard | (1L << bitPosition);
    }

    public static boolean isSet(long bitboard, int bitPosition) {
        return (bitboard & (1L << bitPosition)) != 0L;
    }

    public static boolean isUnset(long bitboard, int bitPosition) {
        return (bitboard & (1L << bitPosition)) == 0L;
    }

    public static long moveBit(long bitboard, int oldBitPosition, int newBitPosition) {
        final long eraseOldBitMask = ~(1L << oldBitPosition);
        final long setNewBitValue = 1L << newBitPosition;
        return (bitboard & eraseOldBitMask) | setNewBitValue;
    }

    private static long createRowBitboard(int row) {
        long bitboard = 0L;
        for (int column = 0; column < Board.COLUMN_COUNT; ++column) {
            bitboard |= 1L << BoardSquare.encode(row, column);
        }
        return bitboard;
    }

    private static long createColumnBitboard(int column) {
        long bitboard = 0L;
        for (int row = 0; row < Board.ROW_COUNT; ++row) {
            bitboard |= 1L << BoardSquare.encode(row, column);
        }
        return bitboard;
    }
}
