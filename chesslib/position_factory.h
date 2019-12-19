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
    CastleOptions blackCastleOptions);

}