package com.chesslib.board;

public enum Direction {
    UP(8),
    DOWN(-8);

    public final int boardSquareDelta;

    Direction(int boardSquareDelta) {
        this.boardSquareDelta = boardSquareDelta;
    }
}
