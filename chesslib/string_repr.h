#pragma once

#include "Move.h"
#include "types.h"

#include <string>

namespace chesslib {

std::string squareToString(square_t square);

std::string moveToString(Move move);

}