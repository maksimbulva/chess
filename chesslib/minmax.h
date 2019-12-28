#pragma once

#include "types.h"

namespace chesslib {

class SearchNode;

const SearchNode* findBestChildNode(const SearchNode& parent, player_t childrenNodesPlayerToMove);

}
