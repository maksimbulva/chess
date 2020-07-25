package com.chesslib.position;

public class CastlingAvailability {
    private static final int CAN_CASTLE_SHORT = 1;
    private static final int CAN_CASTLE_LONG = 1;

    public static final int NONE = 0;
    public static final int SHORT_ONLY = CAN_CASTLE_SHORT;
    public static final int LONG_ONLY = CAN_CASTLE_LONG;
    public static final int ANY = CAN_CASTLE_SHORT | CAN_CASTLE_LONG;

    public static int addCanCastleShort(int encoded) {
        return encoded | CAN_CASTLE_SHORT;
    }

    public static int addCanCastleLong(int encoded) {
        return encoded | CAN_CASTLE_LONG;
    }
}
