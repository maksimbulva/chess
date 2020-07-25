package com.chesslib.move;

import com.chesslib.PieceCode;
import com.chesslib.Player;
import com.chesslib.board.Bitboard;
import com.chesslib.board.Board;
import com.chesslib.board.Direction;
import com.chesslib.board.PieceBitboards;
import com.chesslib.position.Position;

import java.util.List;

public final class MoveGenerator {

    private static final long PAWN_PROMOTION_RANKS_BITBOARD = Bitboard.ROW1 | Bitboard.ROW8;

    private final Position position;
    private final Board board;

    private Player myPlayer;
    private Player otherPlayer;
    private Direction pawnMoveDirection;
    private long pawnsInitialRowBitboard;
    private long allPiecesBitboard;
    private final MoveGenerationFilter movesFilter = MoveGenerationFilter.ALL_MOVES;

    public MoveGenerator(Position position) {
        this.position = position;
        board = position.board;
    }

    public void generatePseudoLegalMoves(List<Integer> moves) {
        updateCachedValues();

        final PieceBitboards bitboards = board.getPieceBitboards(myPlayer);
        final long allPiecesBitboard = board.getAllPiecesBitboard();
        final long myPiecesBitboard = bitboards.getAllPieces();
        final long notMyPiecesBitboard = ~myPiecesBitboard;
        final long otherPiecesBitboard = board.getPieceBitboards(otherPlayer).getAllPieces();

        int originSquare = 0;
        while (originSquare < Board.SQUARE_COUNT) {
            final long shiftedBitboard = (myPiecesBitboard >>> originSquare);
            final long lowerByte = shiftedBitboard & 0xFFL;
            if (lowerByte == 0L) {
                originSquare += 8;
                continue;
            }
            final int leastSignificantBit = Bitboard.getLeastSignificantBit((int)lowerByte);
            originSquare += leastSignificantBit;
            final int pieceToMove = board.getPieceAt(originSquare);
            switch (pieceToMove) {
                case PieceCode.PAWN:
                    generatePawnMoves(moves, originSquare);
                    break;
                case PieceCode.KNIGHT:
                    generateKnightMoves(moves, originSquare, notMyPiecesBitboard, otherPiecesBitboard);
                    break;
                // TODO
            }
            ++originSquare;
        }
    }

    private void updateCachedValues() {
        myPlayer = position.getPlayerToMove();
        if (myPlayer == Player.BLACK) {
            otherPlayer = Player.WHITE;
            pawnMoveDirection = Direction.DOWN;
            pawnsInitialRowBitboard = Bitboard.ROW7;
        } else {
            otherPlayer = Player.BLACK;
            pawnMoveDirection = Direction.UP;
            pawnsInitialRowBitboard = Bitboard.ROW2;
        }
        allPiecesBitboard = board.getAllPiecesBitboard();
    }

    private void generatePawnMoves(
            List<Integer> moves,
            int originSquare
    ) {
        final int moveTemplate = originSquare | MoveEncoder.encodePiece(PieceCode.PAWN);

        final int singleMoveForwardSquare = originSquare + pawnMoveDirection.boardSquareDelta;
        if (movesFilter == MoveGenerationFilter.ALL_MOVES && isEmpty(singleMoveForwardSquare)) {
            addPawnMove(moves, moveTemplate, singleMoveForwardSquare);
            if (Bitboard.isSet(pawnsInitialRowBitboard, originSquare)) {
                final int doubleMoveForwardSquare = singleMoveForwardSquare + pawnMoveDirection.boardSquareDelta;
                if (isEmpty(doubleMoveForwardSquare)) {
                    moves.add(moveTemplate | MoveEncoder.encodeDestSquare(doubleMoveForwardSquare));
                }
            }
        }
    }

    private void addPawnMove(List<Integer> moves, int moveTemplate, int destSquare) {
        moveTemplate |= MoveEncoder.encodeDestSquare(destSquare);
        if (Bitboard.isSet(PAWN_PROMOTION_RANKS_BITBOARD, destSquare)) {
            generatePromotions(moves, moveTemplate);
        } else {
            moves.add(moveTemplate);
        }
    }

    private static void generatePromotions(List<Integer> moves, int moveTemplate) {
        for (int promotionPiece = PieceCode.QUEEN; promotionPiece >= PieceCode.KNIGHT; --promotionPiece) {
            moves.add(moveTemplate | MoveEncoder.encodePawnPromotion(promotionPiece));
        }
    }

    private void generateKnightMoves(
            List<Integer> moves,
            int originSquare,
            long invMyPiecesBitboard,
            long otherPiecesBitboard
    ) {
        final int moveTemplate = originSquare | MoveEncoder.encodePiece(PieceCode.KNIGHT);
        final long originSquareBit = 1L << originSquare;

        final long moveDownDownLeftBit = (originSquareBit & Bitboard.NOT_COLUMN_A) >>> 17;
        if ((moveDownDownLeftBit & invMyPiecesBitboard) != 0L) {
            moves.add(createMove(moveTemplate, originSquare - 17, otherPiecesBitboard));
        }

        final long moveDownDownRightBit = (originSquareBit & Bitboard.NOT_COLUMN_H) >>> 15;
        if ((moveDownDownRightBit & invMyPiecesBitboard) != 0L) {
            moves.add(createMove(moveTemplate, originSquare - 15, otherPiecesBitboard));
        }

        final long moveDownLeftLeftBit = (originSquareBit & Bitboard.NOT_COLUMN_AB) >>> 10;
        if ((moveDownLeftLeftBit & invMyPiecesBitboard) != 0L) {
            moves.add(createMove(moveTemplate, originSquare - 10, otherPiecesBitboard));
        }

        final long moveDownRightRightBit = (originSquareBit & Bitboard.NOT_COLUMN_GH) >>> 6;
        if ((moveDownRightRightBit & invMyPiecesBitboard) != 0L) {
            moves.add(createMove(moveTemplate, originSquare - 6, otherPiecesBitboard));
        }

        final long moveUpLeftLeftBit = (originSquareBit & Bitboard.NOT_COLUMN_AB) << 6;
        if ((moveUpLeftLeftBit & invMyPiecesBitboard) != 0L) {
            moves.add(createMove(moveTemplate, originSquare + 6, otherPiecesBitboard));
        }

        final long moveUpRightRightBit = (originSquareBit & Bitboard.NOT_COLUMN_GH) << 10;
        if ((moveUpRightRightBit & invMyPiecesBitboard) != 0L) {
            moves.add(createMove(moveTemplate, originSquare + 10, otherPiecesBitboard));
        }

        final long moveUpUpLeft = (originSquareBit & Bitboard.NOT_COLUMN_A) << 15;
        if ((moveUpUpLeft & invMyPiecesBitboard) != 0L) {
            moves.add(createMove(moveTemplate, originSquare + 15, otherPiecesBitboard));
        }

        final long moveUpUpRight = (originSquareBit * Bitboard.NOT_COLUMN_H) << 17;
        if ((moveUpUpRight & invMyPiecesBitboard) != 0L) {
            moves.add(createMove(moveTemplate, originSquare + 17, otherPiecesBitboard));
        }
    }

    private int createMove(int moveTemplate, final int destSquare, final long otherPiecesBitboard) {
        moveTemplate |= MoveEncoder.encodeDestSquare(destSquare);
        final long destSquareBit = 1L << destSquare;
        if ((otherPiecesBitboard & destSquareBit) != 0L) {
            moveTemplate |= MoveEncoder.encodeCapture(board.getPieceAt(destSquare));
        }
        return moveTemplate;
    }

    private boolean isEmpty(int boardSquare) {
        return Bitboard.isUnset(allPiecesBitboard, boardSquare);
    }
}
