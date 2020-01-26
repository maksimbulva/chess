#pragma once

#include "Move.h"
#include "types.h"

#include <string>

namespace chesslib {

struct ParsedMove {
    const square_t originSquare;
    const square_t destSquare;
    const piece_type_t promoteToPieceType;
};

square_t parseSquare(std::string squareString);

ParsedMove parseCoordinateNotation(const std::string& moveString);

std::string toCoordinateNotation(Move move);

}
