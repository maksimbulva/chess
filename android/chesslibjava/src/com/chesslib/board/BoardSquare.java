package com.chesslib.board;

public abstract class BoardSquare {

    public static int encode(int row, int column) {
        return (row << 3) | column;
    }

    public static int getRow(int encodedSquare) {
        return encodedSquare >> 3;
    }

    public static int getColumn(int encodedSquare) {
        return encodedSquare & 7;
    }

    public static int valueOf(String moveString) {
        final int columnChar = Character.toLowerCase(moveString.charAt(0));
        final int rowChar = moveString.charAt(1);
        if (columnChar >= 'a' && columnChar <= 'h' && rowChar >= '1' && rowChar <= '8') {
            return encode(rowChar - '1', columnChar - 'a');
        } else {
            throw new IllegalArgumentException("Cannot convert to board square: " + moveString);
        }
    }
}
