#pragma once

#include "Board.h"
#include "board_utils.h"
#include "MovesCollection.h"
#include "OptionalColumn.h"
#include "PositionFlags.h"
#include "PositionHistory.h"

#include <vector>

namespace chesslib {

class Position
{
public:
    Position(
        square_t blackKingSquare,
        square_t whiteKingSquare,
        player_t playerToMove);

    void fillWithLegalMoves(MovesCollection& moves) const;

    void fillWithPseudoLegalMoves(MovesCollection& moves) const;

    player_t getPlayerToMove() const
    {
        return positionFlags_.getPlayerToMove();
    }

    player_t getOtherPlayer() const
    {
        return 1 - getPlayerToMove();
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

    bool isCanUndoMove() const
    {
        return !history_.empty();
    }

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
    PositionFlags positionFlags_;

    // TODO: consider another type of container if needed
    std::vector<PositionHistory> history_;
};

}
