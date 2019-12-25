#pragma once

#include "directions.h"
#include "RayIterator.h"

namespace chesslib {

class Board;

bool isSquareAttacked(square_t target, const Board& board, player_t attacker);

bool isSquareAttackedFromSpecificSquare(
    square_t origin,
    square_t target,
    const Board& board,
    player_t attacker);

bool isSquareSlideAttackedThroughSpecificSquare(
    square_t intermediateSquare,
    square_t target,
    const Board& board,
    player_t attacker);

}
