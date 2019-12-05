#pragma once

#include "types.h"

namespace chesslib {

struct PieceOnBoard
{
    const player_t player;
    const piece_type_t pieceType;
    const square_t square;
};

}
