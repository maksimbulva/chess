package com.chesslib.board;

import com.chesslib.Piece;
import com.chesslib.Player;

public final class PieceOnBoard {
    public final Player player;
    public final Piece piece;
    public final int boardSquare;

    public PieceOnBoard(Player player, Piece piece, int boardSquare) {
        this.player = player;
        this.piece = piece;
        this.boardSquare = boardSquare;
    }
}
