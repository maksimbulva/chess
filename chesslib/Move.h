#pragma once

#include "types.h"

namespace chesslib {

struct Move {
public:
    Move(piece_type_t pieceType, square_t originSquare, square_t destSquare)
        : encoded_(encode(pieceType, originSquare, destSquare))
    {
    }

    Move(piece_type_t pieceType, square_t originSquare, square_t destSquare, encoded_move_t flags)
        : encoded_(encode(pieceType, originSquare, destSquare) | flags)
    {
    }

    Move(
        piece_type_t pieceType,
        square_t originSquare,
        square_t destSquare,
        piece_type_t promoteToPiece,
        encoded_move_t flags
    )
        : encoded_(encode(pieceType, originSquare, destSquare)
            | ((static_cast<encoded_move_t>(promoteToPiece)) << 16)
            | flags)
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

public:
    static constexpr encoded_move_t Capture = (static_cast<encoded_move_t>(1)) << 20;
    static constexpr encoded_move_t EnPassantCapture = (static_cast<encoded_move_t>(1)) << 21;
    static constexpr encoded_move_t Promotion = (static_cast<encoded_move_t>(1)) << 24;

private:
    encoded_move_t encode(piece_type_t pieceType, square_t originSquare, square_t destSquare)
    {
        return static_cast<encoded_move_t>(originSquare | (destSquare << 6) | (pieceType << 12));
    }

    static constexpr encoded_move_t SQUARE_MASK = 63;

    const encoded_move_t encoded_;
};

}
