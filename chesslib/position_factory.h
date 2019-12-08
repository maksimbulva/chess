#pragma once

#include "PieceOnBoard.h"
#include "Position.h"

#include <vector>

namespace chesslib {

Position createPosition(std::vector<PieceOnBoard> pieces);

}