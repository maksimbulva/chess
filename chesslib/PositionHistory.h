#pragma once

#include "Move.h"
#include "PositionFlags.h"

namespace chesslib {

struct PositionHistory {
public:
    PositionHistory(Move move, PositionFlags positionFlags)
        : move_(move)
        , positionFlags_(positionFlags)
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

private:
    Move move_;
    PositionFlags positionFlags_;
};

}
