#pragma once

#include "directions.h"
#include "types.h"

#include <algorithm>

namespace chesslib {

struct MoveDelta {
    MoveDelta(square_t origin, square_t dest)
        : deltaRow(getRow(dest) - getRow(origin))
        , deltaColumn(getColumn(dest) - getColumn(origin))
    {
    }

    bool isPawnDelta(player_t player) const
    {
        if (deltaColumn == -1 || deltaColumn == 1) {
            if (player == Black) {
                return deltaRow == -1;
            }
            else {
                return deltaRow == 1;
            }
        }
        return false;
    }

    bool isKnightDelta() const
    {
        const square_t absDeltaRow = std::abs(deltaRow);
        if (absDeltaRow == 1 || absDeltaRow == 2) {
            const square_t absDeltaColumn = std::abs(deltaColumn);
            return absDeltaRow + absDeltaColumn == 3;
        }
        return false;
    }

    bool isKingDelta() const
    {
        return deltaRow >= -1 && deltaRow <= 1 && deltaColumn >= -1 && deltaColumn <= 1;
    }

    square_t toBishiopDirection() const
    {
        if (deltaRow == deltaColumn) {
            return deltaRow > 0 ? DIRECTION_UP_RIGHT : DIRECTION_DOWN_LEFT;
        }
        else if (deltaRow == -deltaColumn) {
            return deltaRow > 0 ? DIRECTION_UP_LEFT : DIRECTION_DOWN_RIGHT;
        }
        return DIRECTION_NONE;
    }

    square_t toRookDirection() const
    {
        if (deltaRow == 0) {
            return deltaColumn > 0 ? DIRECTION_RIGHT : DIRECTION_LEFT;
        }
        else if (deltaColumn == 0) {
            return deltaRow > 0 ? DIRECTION_UP : DIRECTION_DOWN;
        }
        return DIRECTION_NONE;
    }

    const square_t deltaRow;
    const square_t deltaColumn;
};

}
