#pragma once

#include "types.h"

namespace chesslib {

using bitboard_t = uint64_t;

constexpr inline bitboard_t generateValidOriginsForDelta(square_t deltaRow, square_t deltaColumn)
{
    bitboard_t result = 0;

    for (square_t origin = 0; origin < 64; ++origin) {
        const auto row = getRow(origin) + deltaRow;
        const auto column = getColumn(origin) + deltaColumn;
        if (row >= 0 && row < ROW_COUNT && column >= 0 && column < COLUMN_COUNT) {
            result |= (static_cast<bitboard_t>(1)) << origin;
        }
    }

    return result;
}

}
