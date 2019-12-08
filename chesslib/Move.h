#pragma once

#include "types.h"

namespace chesslib {

struct Move {
public:
    Move(square_t originSquare, square_t destSquare)
        : encoded_(encode(originSquare, destSquare))
    {
    }
    
    square_t getOriginSquare() const
    {
        return encoded_ & SQUARE_MASK;
    }

    square_t getDestSquare() const
    {
        return (encoded_ >> 6) & SQUARE_MASK;
    }

private:
    encoded_move_t encode(square_t originSquare, square_t destSquare)
    {
        return static_cast<encoded_move_t>(originSquare | (destSquare << 6));
    }

    static constexpr encoded_move_t SQUARE_MASK = 63;

    const encoded_move_t encoded_;
};

}
