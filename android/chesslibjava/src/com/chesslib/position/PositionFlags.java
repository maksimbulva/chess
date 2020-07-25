package com.chesslib.position;

public abstract class PositionFlags {

    private static final int PLAYER_MASK = 1;
    private static final int CASTLE_OPTIONS_MASK = 3;
    private static final int EN_PASSANT_COLUMN_SHIFT = 6;
    private static final int EN_PASSANT_COLUMN_MASK = 15 << EN_PASSANT_COLUMN_SHIFT;
    private static final int IS_IN_CHECK_SHIFT = 10;
    private static final int IS_IN_CHECK_MASK = 3 << IS_IN_CHECK_SHIFT;

    public static int getPlayerToMove(int flags) {
        return flags & PLAYER_MASK;
    }

    public static int updatePlayerToMove(int playerToMove, int flags) {
        flags &= ~PLAYER_MASK;
        return flags | playerToMove;
    }
}
