#pragma once

#include "Board.h"
#include "board_utils.h"
#include "MovesCollection.h"
#include "OptionalColumn.h"
#include "PositionFlags.h"
#include "PositionHistory.h"
#include "PositionMoveCounters.h"
#include "squares.h"

#include <vector>

namespace chesslib {

class Position
{
public:
    enum class MoveGenerationFilter {
        AllMoves,
        CapturesOnly
    };

public:
    Position(
        square_t blackKingSquare,
        square_t whiteKingSquare,
        player_t playerToMove,
        uint32_t halfmoveClock,
        uint32_t fullmoveNumber);

    void fillWithPseudoLegalMoves(MovesCollection& moves, MoveGenerationFilter movesFilter);

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
        return positionFlags_.getEnPassantColumn();
    }

    void setEnPassantColumn(OptionalColumn column)
    {
        positionFlags_.setEnPassantColumn(column);
    }

    CastleOptions getCastleOptions(player_t player) const
    {
        return positionFlags_.getCastleOptions(player);
    }

    void setCastleOptions(player_t player, CastleOptions castleOptions)
    {
        positionFlags_.setCastleOptions(player, castleOptions);
    }

    void optimizeCastleOptions();

    const Board& getBoard() const
    {
        return board_;
    }

    bool isValid() const;

    bool isInCheck();

    void addPiece(const PieceOnBoard& piece);

    void playMove(const Move& move);

    bool isCanUndoMove() const
    {
        return !history_.empty();
    }

    void undoMove();

private:

    void fillWithPseudoLegalMoves(
        MovesCollection& moves,
        MoveGenerationFilter movesFilter,
        bool isInCheck) const;

    void fillWithPawnMoves(
        square_t pawnSquare,
        MovesCollection& moves,
        MoveGenerationFilter movesFilter) const;

    void fillWithKnightMoves(
        square_t knightSquare,
        MovesCollection& moves,
        MoveGenerationFilter movesFilter) const;

    void fillWithKingMoves(
        square_t kingSquare,
        MovesCollection& moves,
        MoveGenerationFilter movesFilter,
        bool isInCheck) const;

    void fillWithSlideMoves(
        piece_type_t pieceType,
        RayIterator RayIterator,
        MovesCollection& moves,
        MoveGenerationFilter movesFilter) const;

    bool isRecentMovePutsEnemyKingInCheck(Move recentMove) const;

private:
    Board board_;
    PositionFlags positionFlags_;
    PositionMoveCounters moveCounters_;

    // TODO: consider another type of container if needed
    std::vector<PositionHistory> history_;
};

}
