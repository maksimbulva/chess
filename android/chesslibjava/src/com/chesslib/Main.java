package com.chesslib;

import com.chesslib.position.Position;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        for (int depthPly = 1; depthPly <= 2; ++depthPly) {
            final long legalMoveCount = countLegalMoves(
                    "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1",
                    depthPly);
            System.out.println("Generated moves: " + legalMoveCount);
        }
    }

    private static long countLegalMoves(final String fenString, final int depthPly)
    {
        Position initialPosition = Fen.decode(fenString);
        return countLegalMovesRecursively(initialPosition, depthPly);
    }

    private static long countLegalMovesRecursively(final Position position, final int depthPly)
    {
        if (depthPly == 0) {
            return 1;
        }

        long result = 0;

        final List<Integer> moves = position.getLegalMoves();
        for (Integer move : moves) {
            position.playMove(move);
            if (position.isValid()) {
                if (depthPly == 1) {
                    ++result;
                }
                else {
                    result += countLegalMovesRecursively(position, depthPly - 1);
                }
            }
            position.undoMove();
        }

        return result;
    }
}
