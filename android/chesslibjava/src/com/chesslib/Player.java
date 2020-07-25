package com.chesslib;

public enum Player {
    BLACK(PlayerCode.BLACK),
    WHITE(PlayerCode.WHITE);

    private final int playerCode;

    Player(int playerCode) {
        this.playerCode = playerCode;
    }

    public int getPlayerCode() {
        return playerCode;
    }

    public Player getOtherPlayer() {
        return this == BLACK ? WHITE : BLACK;
    }
}
