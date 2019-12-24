#pragma once

#include "Move.h"
#include "PositionFlags.h"
#include "PositionMoveCounters.h"

namespace chesslib {

struct PositionHistory {
public:
    PositionHistory(Move move, PositionFlags positionFlags, PositionMoveCounters moveCounters)
        : move_(move)
        , positionFlags_(positionFlags)
        , moveCounters_(moveCounters)
    {
    }

    Move getMove() const
    {
        return move_;
    }

    PositionFlags getPositionFlags() const
    {
        return positionFlags_;
    }

    PositionMoveCounters getPositionMoveCounters() const
    {
        return moveCounters_;
    }

private:
    Move move_;
    PositionFlags positionFlags_;
    PositionMoveCounters moveCounters_;
};

}
