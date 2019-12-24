#pragma once

#include "types.h"

namespace chesslib {

struct BoardSquare {
    static constexpr uint8_t NO_NODE = 255;

    player_t getPlayer() const
    {
        return static_cast<player_t>(coloredPiece) & 1;
    }

    piece_type_t getPieceType() const
    {
        return static_cast<piece_type_t>(coloredPiece) >> 1;
    }

    void setPiece(player_t player, piece_type_t pieceType)
    {
        coloredPiece = static_cast<uint8_t>(player | (pieceType << 1));
    }

    bool isEmpty() const
    {
        return coloredPiece == 0;
    }

    bool isNotEmpty() const
    {
        return !isEmpty();
    }

    void makeEmpty()
    {
        coloredPiece = 0;
        prevNode = NO_NODE;
        nextNode = NO_NODE;
    }

    uint8_t square = 0;
    uint8_t coloredPiece = 0;
    uint8_t prevNode = NO_NODE;
    uint8_t nextNode = NO_NODE;
};

}
