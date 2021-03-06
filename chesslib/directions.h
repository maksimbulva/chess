#pragma once

#include "types.h"

namespace chesslib {

constexpr inline square_t getDelta(fastint deltaRow, fastint deltaColumn)
{
    return static_cast<square_t>(deltaRow * 8 + deltaColumn);
}

constexpr square_t DIRECTION_NONE = getDelta(0, 0);

constexpr square_t DIRECTION_LEFT = getDelta(0, -1);
constexpr square_t DIRECTION_RIGHT = getDelta(0, 1);
constexpr square_t DIRECTION_UP = getDelta(1, 0);
constexpr square_t DIRECTION_DOWN = getDelta(-1, 0);

constexpr square_t DIRECTION_UP_LEFT = getDelta(1, -1);
constexpr square_t DIRECTION_UP_RIGHT = getDelta(1, 1);
constexpr square_t DIRECTION_DOWN_LEFT = getDelta(-1, -1);
constexpr square_t DIRECTION_DOWN_RIGHT = getDelta(-1, 1);

}
