package com.chesslib.board;

import com.chesslib.Piece;
import com.chesslib.Player;
import com.chesslib.PlayerCode;

import java.util.Collection;

public final class Board {
    public static final int ROW_COUNT = 8;
    public static final int COLUMN_COUNT = 8;
    public static final int SQUARE_COUNT = ROW_COUNT * COLUMN_COUNT;

    private long allPiecesBitboard;
    private final PieceBitboards[] bitboards = new PieceBitboards[PlayerCode.PLAYER_COUNT];

    /**
     * Indicates which piece occupies the square provided that the square is occupied.
     * For empty squares the result is undefined.
     */
    private final int[] pieces = new int[SQUARE_COUNT];

    public Board(Collection<PieceOnBoard> pieces) {
        for (int i = 0; i < bitboards.length; ++i) {
            bitboards[i] = new PieceBitboards();
        }

        for (PieceOnBoard piece : pieces) {
            if (piece.piece == Piece.KING) {
                addKing(piece);
            }
        }

        for (PieceOnBoard piece : pieces) {
            if (piece.piece != Piece.KING) {
                addPiece(piece);
            }
        }
    }

    public long getAllPiecesBitboard() {
        return allPiecesBitboard;
    }

    public PieceBitboards getPieceBitboards(Player player) {
        return bitboards[player.getPlayerCode()];
    }

    public int getPieceAt(int boardSquare) {
        return pieces[boardSquare];
    }

    public boolean isEmpty(int boardSquare) {
        return Bitboard.isUnset(allPiecesBitboard, boardSquare);
    }

    public void movePiece(Player player, int piece, int oldBoardSquare, int newBoardSquare) {
        bitboards[player.getPlayerCode()].movePiece(piece, oldBoardSquare, newBoardSquare);
        allPiecesBitboard = Bitboard.moveBit(allPiecesBitboard, oldBoardSquare, newBoardSquare);
    }

    private void addKing(PieceOnBoard king) {
        requireIsEmpty(king.boardSquare);
        allPiecesBitboard = Bitboard.setBit(allPiecesBitboard, king.boardSquare);
        getPieceBitboards(king.player).addPiece(Piece.KING, king.boardSquare);
    }

    private void addPiece(PieceOnBoard pieceToAdd) {
        requireIsEmpty(pieceToAdd.boardSquare);

        allPiecesBitboard = Bitboard.setBit(allPiecesBitboard, pieceToAdd.boardSquare);
        getPieceBitboards(pieceToAdd.player).addPiece(pieceToAdd.piece, pieceToAdd.boardSquare);

        pieces[pieceToAdd.boardSquare] = pieceToAdd.piece.pieceCode;
    }

    private void requireIsEmpty(int boardSquare) {
        if (!isEmpty(boardSquare)) {
            throw new IllegalStateException("Board square is already occupied");
        }
    }
}
