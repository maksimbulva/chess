#pragma once

#include "Board.h"
#include "MovesCollection.h"

namespace chesslib {

class Position
{
public:
    Position(
        square_t blackKingSquare,
        square_t whiteKingSquare);

    void addPiece(const PieceOnBoard& piece);

    void fillWithLegalMoves(MovesCollection& moves) const;

    player_t getPlayerToMove() const
    {
        // TODO
        return White;
    }

private:

    void fillWithPseudoLegalMoves(MovesCollection& moves) const;

    void fillWithPawnMoves(square_t pawnSquare, MovesCollection& moves) const;
    void fillWithKnightMoves(square_t knightSquare, MovesCollection& moves) const;


private:
    Board board_;
};

}
