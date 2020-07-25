package com.chesslib;

import com.chesslib.board.Board;
import com.chesslib.board.BoardSquare;
import com.chesslib.board.PieceOnBoard;
import com.chesslib.position.CastlingAvailability;
import com.chesslib.position.Position;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Fen {
    private static final String ROW_SEPARATOR = "/";
    private static final char BLACK_TO_MOVE = 'b';
    private static final char WHITE_TO_MOVE = 'w';
    private static final char CAN_CASTLE_SHORT = 'k';
    private static final char CAN_CASTLE_LONG = 'q';

    // Decodes chess positions from Forsyth–Edwards notation format
    public static Position decode(String encoded) {
        final String[] splitted = encoded.split(" ");
        final int halfmoveClock = 4 < splitted.length ? Integer.parseInt(splitted[4]) : 0;
        final int fullmoveNumber = 5 < splitted.length ? Integer.parseInt(splitted[5]) : 0;

        return new Position(
                decodeBoard(splitted[0]),
                decodedPlayerToMove(splitted[1]),
                decodeCastlingAvailability(splitted[2], Player.BLACK),
                decodeCastlingAvailability(splitted[2], Player.WHITE),
                decodeEnPassantCaptureColumn(splitted[3]),
                halfmoveClock,
                fullmoveNumber
        );
    }

    private static Board decodeBoard(String encoded) {
        final String[] encodedRows = encoded.split(ROW_SEPARATOR);
        if (encodedRows.length != Board.ROW_COUNT) {
            throw new IllegalStateException();
        }

        final List<PieceOnBoard> decodedPieces = new ArrayList<>();
        int row = Board.ROW_COUNT - 1;
        for (String encodedRow : encodedRows) {
            decodedPieces.addAll(decodeBoardRow(encodedRow, row));
            --row;
        }

        return new Board(decodedPieces);
    }

    private static Collection<PieceOnBoard> decodeBoardRow(String encoded, int row) {
        final List<PieceOnBoard> decodedPieces = new ArrayList<>();
        int column = 0;
        for (int i = 0; i < encoded.length(); ++i) {
            final char c = encoded.charAt(i);
            if (Character.isDigit(c)) {
                column += Integer.parseInt(String.valueOf(c));
            } else {
                decodedPieces.add(new PieceOnBoard(
                        decodePlayer(c),
                        decodePiece(c),
                        BoardSquare.encode(row, column)
                ));
                ++column;
            }
        }
        if (column != Board.COLUMN_COUNT) {
            throw new IllegalStateException();
        }
        return decodedPieces;
    }

    private static Player decodePlayer(char encodedPiece) {
        if (Character.isLowerCase(encodedPiece)) {
            return Player.BLACK;
        } else {
            return Player.WHITE;
        }
    }

    private static Piece decodePiece(char encodedPiece) {
        switch (Character.toLowerCase(encodedPiece)) {
            case 'p':
                return Piece.PAWN;
            case 'n':
                return Piece.KNIGHT;
            case 'b':
                return Piece.BISHOP;
            case 'r':
                return Piece.ROOK;
            case 'q':
                return Piece.QUEEN;
            case 'k':
                return Piece.KING;
            default:
                throw new IllegalArgumentException("Illegal FEN piece character " + encodedPiece);
        }
    }

    private static Player decodedPlayerToMove(String encoded) {
        switch (Character.toLowerCase(encoded.charAt(0))) {
            case BLACK_TO_MOVE:
                return Player.BLACK;
            case WHITE_TO_MOVE:
                return Player.WHITE;
            default:
                throw new IllegalArgumentException("Illegal FEN player to move " + encoded);
        }
    }

    private static int decodeCastlingAvailability(String encoded, Player player) {
        int decodedValue = CastlingAvailability.NONE;
        final char shortCastleAvailable = (player == Player.BLACK)
                ? CAN_CASTLE_SHORT : Character.toUpperCase(CAN_CASTLE_SHORT);
        if (encoded.indexOf(shortCastleAvailable) >= 0) {
            decodedValue = CastlingAvailability.addCanCastleShort(decodedValue);
        }
        final char longCastleAvailable = (player == Player.BLACK)
                ? CAN_CASTLE_LONG : Character.toUpperCase(CAN_CASTLE_LONG);
        if (encoded.indexOf(longCastleAvailable) >= 0) {
            decodedValue = CastlingAvailability.addCanCastleLong(decodedValue);
        }
        return decodedValue;
    }

    private static Integer decodeEnPassantCaptureColumn(String encoded) {
        if (encoded.length() == 2) {
            final int enPassantCaptureSquare = BoardSquare.valueOf(encoded);
            return BoardSquare.getColumn(enPassantCaptureSquare);
        } else {
            return null;
        }
    }
}
