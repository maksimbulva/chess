#pragma once

#include "Position.h"
#include "SearchNode.h"

namespace chesslib {

class SearchTree {
public:
    SearchTree(const Position& initialPosition);

    SearchNode& getRoot()
    {
        return root_;
    }

private:
    Position initialPosition_;
    SearchNode root_;
};

}
