#pragma once

#include "OptionalColumn.h"
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

    OptionalColumn getEnPassantColumn() const
    {
        auto encoded = (encoded_ & EN_PASSANT_COLUMN_MASK) >> EN_PASSANT_COLUMN_SHIFT;
        return OptionalColumn::fromEncoded(encoded);
    }

    void setEnPassantColumn(OptionalColumn column)
    {
        encoded_ &= ~EN_PASSANT_COLUMN_MASK;
        encoded_ |= column.getEncodedValue() << EN_PASSANT_COLUMN_SHIFT;
    }

private:
    static constexpr encoded_position_flags_t PLAYER_MASK = 1;
    static constexpr int EN_PASSANT_COLUMN_SHIFT = 6;
    static constexpr encoded_position_flags_t EN_PASSANT_COLUMN_MASK = 15 << EN_PASSANT_COLUMN_SHIFT;

    encoded_position_flags_t encoded_;
};

}
