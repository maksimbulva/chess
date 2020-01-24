#pragma once

#include "bitboard.h"
#include "types.h"

namespace chesslib {

struct PawnEvaluationFactors {
    bitboard_t pawnsBitboard;
    uint32_t pawnsColumnMask;
    uint32_t columnsWithDoubledPawnsCounter;
    uint32_t hasPawnOnPrePromotionRow;
    uint32_t pawnIslandCount;
};

}
