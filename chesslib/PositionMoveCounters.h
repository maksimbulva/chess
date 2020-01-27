#pragma once

#include <cstdint>

namespace chesslib {

struct PositionMoveCounters {
public:
    PositionMoveCounters(uint32_t inHalfmoveClock, uint32_t inFullmoveNumber)
        : halfmoveClock(inHalfmoveClock)
        , fullmoveNumber(inFullmoveNumber)
    {
    }

    uint32_t halfmoveClock;
    uint32_t fullmoveNumber;
};

}
