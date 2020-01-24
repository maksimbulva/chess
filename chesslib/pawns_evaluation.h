#pragma once

#include "bitboard.h"
#include "PawnEvaluationFactors.h"
#include "types.h"

namespace chesslib {

class Board;

bitboard_t getPawnsBitboard(const Board& board, player_t player);

void updatePawnFactors(bitboard_t pawnsBitboard, player_t player, PawnEvaluationFactors& pawnFactors);

void initializePawnIslandCountByMask();

}
