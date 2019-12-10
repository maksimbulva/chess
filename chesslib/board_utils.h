#pragma once

#include "types.h"

#include <algorithm>

namespace chesslib {

constexpr inline square_t getDelta(fastint deltaRow, fastint deltaColumn)
{
    return static_cast<square_t>(deltaRow * 8 + deltaColumn);
}

constexpr square_t DIRECTION_LEFT = getDelta(0, -1);
constexpr square_t DIRECTION_RIGHT = getDelta(0, 1);
constexpr square_t DIRECTION_UP = getDelta(1, 0);
constexpr square_t DIRECTION_DOWN = getDelta(-1, 0);

constexpr square_t DIRECTION_UP_LEFT = getDelta(1, -1);
constexpr square_t DIRECTION_UP_RIGHT = getDelta(1, 1);
constexpr square_t DIRECTION_DOWN_LEFT = getDelta(-1, -1);
constexpr square_t DIRECTION_DOWN_RIGHT = getDelta(-1, 1);

class RayIterator
{
public:
    RayIterator(square_t origin, square_t direction, fastint remainingLength)
        : currentSquare_(origin)
        , direction_(direction)
        , remainingLength_(remainingLength)
    {
    }

    bool hasNext() const
    {
        return remainingLength_ != 0;
    }

    square_t currentSquare() const
    {
        return currentSquare_;
    }

    void operator++()
    {
        currentSquare_ += direction_;
        --remainingLength_;
    }

private:
    square_t currentSquare_;
    const square_t direction_;
    fastint remainingLength_;
};

template<square_t direction>
fastint calculateMaxRayLenght(square_t origin);

template<square_t direction>
RayIterator createRayIterator(square_t origin)
{
    return RayIterator(origin, direction, calculateMaxRayLenght<direction>(origin));
}

template<>
inline fastint calculateMaxRayLenght<DIRECTION_LEFT>(square_t origin)
{
    return getColumn(origin);
}

template<>
inline fastint calculateMaxRayLenght<DIRECTION_RIGHT>(square_t origin)
{
    return MAX_COLUMN - getColumn(origin);
}

template<>
inline fastint calculateMaxRayLenght<DIRECTION_UP>(square_t origin)
{
    return MAX_ROW - getRow(origin);
}

template<>
inline fastint calculateMaxRayLenght<DIRECTION_DOWN>(square_t origin)
{
    return getRow(origin);
}

template<>
inline fastint calculateMaxRayLenght<DIRECTION_UP_LEFT>(square_t origin)
{
    return std::min(
        calculateMaxRayLenght<DIRECTION_UP>(origin),
        calculateMaxRayLenght<DIRECTION_LEFT>(origin));
}

template<>
inline fastint calculateMaxRayLenght<DIRECTION_UP_RIGHT>(square_t origin)
{
    return std::min(
        calculateMaxRayLenght<DIRECTION_UP>(origin),
        calculateMaxRayLenght<DIRECTION_RIGHT>(origin));
}

template<>
inline fastint calculateMaxRayLenght<DIRECTION_DOWN_LEFT>(square_t origin)
{
    return std::min(
        calculateMaxRayLenght<DIRECTION_DOWN>(origin),
        calculateMaxRayLenght<DIRECTION_LEFT>(origin));
}

template<>
inline fastint calculateMaxRayLenght<DIRECTION_DOWN_RIGHT>(square_t origin)
{
    return std::min(
        calculateMaxRayLenght<DIRECTION_DOWN>(origin),
        calculateMaxRayLenght<DIRECTION_RIGHT>(origin));
}

}
