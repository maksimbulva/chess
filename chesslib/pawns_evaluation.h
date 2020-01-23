#pragma once

#include "bitboard.h"
#include "types.h"

namespace chesslib {

class Board;

bitboard_t getPawnsBitboard(const Board& board, player_t player);

uint32_t getPawnsColumnMask(bitboard_t pawnsBitboard);

}
