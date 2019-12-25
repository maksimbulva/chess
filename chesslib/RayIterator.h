#pragma once

#include "MoveDelta.h"
#include "types.h"

#include <algorithm>

namespace chesslib {

class RayIterator
{
public:
    RayIterator(square_t origin, square_t direction, fastint remainingLength, piece_type_t pieceType)
        : currentSquare_(origin)
        , remainingLength_(remainingLength)
        , direction_(direction)
        , pieceType_(pieceType)
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

    piece_type_t getPieceType() const
    {
        return pieceType_;
    }

    static RayIterator createEmpty()
    {
        return RayIterator(0, DIRECTION_NONE, 0, NoPiece);
    }

private:
    square_t currentSquare_;
    fastint remainingLength_;
    const square_t direction_;
    const piece_type_t pieceType_;
};

template<square_t direction>
RayIterator createRayIterator(square_t origin);

template<>
inline RayIterator createRayIterator<DIRECTION_LEFT>(square_t origin)
{
    fastint length = getColumn(origin);
    return RayIterator(origin, DIRECTION_LEFT, length, Rook);
}

template<>
inline RayIterator createRayIterator<DIRECTION_RIGHT>(square_t origin)
{
    fastint length = MAX_COLUMN - getColumn(origin);
    return RayIterator(origin, DIRECTION_RIGHT, length, Rook);
}

template<>
inline RayIterator createRayIterator<DIRECTION_UP>(square_t origin)
{
    fastint length = MAX_ROW - getRow(origin);
    return RayIterator(origin, DIRECTION_UP, length, Rook);
}

template<>
inline RayIterator createRayIterator<DIRECTION_DOWN>(square_t origin)
{
    fastint length = getRow(origin);
    return RayIterator(origin, DIRECTION_DOWN, length, Rook);
}

template<>
inline RayIterator createRayIterator<DIRECTION_UP_LEFT>(square_t origin)
{
    fastint length = std::min(MAX_ROW - getRow(origin), getColumn(origin));
    return RayIterator(origin, DIRECTION_UP_LEFT, length, Bishop);
}

template<>
inline RayIterator createRayIterator<DIRECTION_UP_RIGHT>(square_t origin)
{
    fastint length = std::min(MAX_ROW - getRow(origin), MAX_COLUMN - getColumn(origin));
    return RayIterator(origin, DIRECTION_UP_RIGHT, length, Bishop);
}

template<>
inline RayIterator createRayIterator<DIRECTION_DOWN_LEFT>(square_t origin)
{
    fastint length = std::min(getRow(origin), getColumn(origin));
    return RayIterator(origin, DIRECTION_DOWN_LEFT, length, Bishop);
}

template<>
inline RayIterator createRayIterator<DIRECTION_DOWN_RIGHT>(square_t origin)
{
    fastint length = std::min(getRow(origin), MAX_COLUMN - getColumn(origin));
    return RayIterator(origin, DIRECTION_DOWN_RIGHT, length, Bishop);
}

inline RayIterator createRayIterator(square_t origin, MoveDelta moveDelta)
{
    if (moveDelta.deltaRow == moveDelta.deltaColumn) {
        if (moveDelta.deltaRow == 0) {
            return RayIterator::createEmpty();
        }
        return moveDelta.deltaRow > 0
            ? createRayIterator<DIRECTION_UP_RIGHT>(origin)
            : createRayIterator<DIRECTION_DOWN_LEFT>(origin);
    }
    else if (moveDelta.deltaRow == -moveDelta.deltaColumn) {
        return moveDelta.deltaRow > 0
            ? createRayIterator<DIRECTION_UP_LEFT>(origin)
            : createRayIterator<DIRECTION_DOWN_RIGHT>(origin);
    }
    else if (moveDelta.deltaRow == 0) {
        return moveDelta.deltaColumn > 0
            ? createRayIterator<DIRECTION_RIGHT>(origin)
            : createRayIterator<DIRECTION_LEFT>(origin);
    }
    else if (moveDelta.deltaColumn == 0) {
        return moveDelta.deltaRow > 0
            ? createRayIterator<DIRECTION_UP>(origin)
            : createRayIterator<DIRECTION_DOWN>(origin);
    }

    return RayIterator::createEmpty();
}

}
