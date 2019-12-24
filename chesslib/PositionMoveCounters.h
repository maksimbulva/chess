#pragma once

#include <cstdint>

namespace chesslib {

struct PositionMoveCounters {
public:
    PositionMoveCounters(uint32_t halfmoveClock, uint32_t fullmoveNumber)
        : encoded_((halfmoveClock << HALFMOVE_CLOCK_SHIFT) | fullmoveNumber)
    {
    }

    uint32_t getHalfmoveClock() const
    {
        return encoded_ >> HALFMOVE_CLOCK_SHIFT;
    }

    uint32_t getFullmoveNumber() const
    {
        return encoded_ & FULLMOVE_NUMBER_MASK;
    }

private:
    static constexpr int HALFMOVE_CLOCK_SHIFT = 16;
    static constexpr uint32_t FULLMOVE_NUMBER_MASK = 0xFFFF;

    uint32_t encoded_;
};

}
