#pragma once

#include "Board.h"
#include "board_utils.h"
#include "MovesCollection.h"
#include "OptionalColumn.h"

namespace chesslib {

class Position
{
public:
    Position(
        square_t blackKingSquare,
        square_t whiteKingSquare);

    void fillWithLegalMoves(MovesCollection& moves) const;

    void fillWithPseudoLegalMoves(MovesCollection& moves) const;

    player_t getPlayerToMove() const
    {
        // TODO
        return White;
    }

    player_t getOtherPlayer() const
    {
        // TODO
        return Black;
    }

    OptionalColumn getEnPassantColumn() const
    {
        // TODO
        return OptionalColumn();
    }

    const Board& getBoard() const
    {
        return board_;
    }

    bool isKingCanBeCaptured() const;

    void addPiece(const PieceOnBoard& piece);

    void playMove(const Move& move);

    void undoMove();

private:

    void fillWithPawnMoves(square_t pawnSquare, MovesCollection& moves) const;
    void fillWithKnightMoves(square_t knightSquare, MovesCollection& moves) const;
    void fillWithKingMoves(square_t kingSquare, MovesCollection& moves) const;

    void fillWithSlideMoves(
        piece_type_t pieceType,
        RayIterator RayIterator,
        MovesCollection& moves) const;

private:
    Board board_;
};

}
