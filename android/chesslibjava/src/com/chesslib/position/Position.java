package com.chesslib.position;

import com.chesslib.Player;
import com.chesslib.board.Board;
import com.chesslib.collection.IntStack;
import com.chesslib.move.Move;
import com.chesslib.move.MoveGenerator;

import java.util.ArrayList;
import java.util.List;

public final class Position {
    private final MoveGenerator moveGenerator;

    public final Board board;

    private Player playerToMove;
    private int positionFlags;

    private final IntStack movesHistory = new IntStack();

    public Position(
            Board board,
            Player playerToMove,
            int blackCastlingAvailability,
            int whiteCastlingAvailability,
            Integer enPassantCaptureColumn,
            int halfmoveClock,
            int fullmoveNumber) {
        this.board = board;
        this.playerToMove = playerToMove;
        moveGenerator = new MoveGenerator(this);
    }

    public Player getPlayerToMove() {
        return playerToMove;
    }

    public boolean isValid() {
        // TODO: implement me
        return true;
    }

    public List<Integer> getLegalMoves() {
        final List<Integer> moves = new ArrayList<>();
        moveGenerator.generatePseudoLegalMoves(moves);
        return moves;
    }

    public void playMove(final int move) {
        if (Move.isCapture(move)) {
            throw new IllegalStateException("Not implemented yet");
        } else {
            board.movePiece(playerToMove, Move.getPiece(move), Move.getOriginSquare(move), Move.getDestSquare(move));
        }

        playerToMove = playerToMove.getOtherPlayer();
        movesHistory.push(move);
    }

    public void undoMove() {
        final int move = movesHistory.pop();
        playerToMove = playerToMove.getOtherPlayer();

        if (Move.isCapture(move)) {
            throw new IllegalStateException("Not implemented yet");
        } else {
            board.movePiece(playerToMove, Move.getPiece(move), Move.getDestSquare(move), Move.getOriginSquare(move));
        }
    }
}
