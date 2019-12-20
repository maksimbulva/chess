#pragma once

#include "Move.h"
#include "types.h"

#include <string>

namespace chesslib {

struct ParsedMove {
    const square_t originSquare;
    const square_t destSquare;
};

square_t parseSquare(std::string squareString);

ParsedMove parseCoordinateNotation(std::string moveString);

std::string toCoordinateNotation(Move move);

}
