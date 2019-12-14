#pragma once

#include "types.h"

namespace chesslib {

class PositionFlags {
public:

    PositionFlags()
        : encoded_(0)
    {
    }

    player_t getPlayerToMove() const
    {
        return static_cast<player_t>(encoded_ & PLAYER_MASK);
    }

    void setPlayerToMove(player_t playerToMove)
    {
        encoded_ &= ~PLAYER_MASK;
        encoded_ |= static_cast<encoded_position_flags_t>(playerToMove);
    }

private:
    static constexpr encoded_position_flags_t PLAYER_MASK = 1;

    encoded_position_flags_t encoded_;
};

}
