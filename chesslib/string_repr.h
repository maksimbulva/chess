#pragma once

#include "Move.h"
#include "PositionHistory.h"
#include "types.h"

#include <string>
#include <vector>

namespace chesslib {

class Board;

std::string playerToString(player_t player);

std::string squareToString(square_t square);

std::string moveToString(Move move);

std::string historyToString(const PositionHistory& history);

std::string historyCollectionToString(const std::vector<PositionHistory>& history);

std::string boardToString(const Board& board);

std::string positionFlagsToString(PositionFlags flags);

}