#pragma once

#include "CastleOptions.h"
#include "PieceOnBoard.h"
#include "Position.h"

#include <vector>

namespace chesslib {

Position createPosition(
    std::vector<PieceOnBoard> pieces,
    player_t playerToMove,
    OptionalColumn enPassantColumn,
    CastleOptions whiteCastleOptions,
    CastleOptions blackCastleOptions,
    uint32_t halfmoveClock,
    uint32_t fullmoveNumber);

}