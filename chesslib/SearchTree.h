#pragma once

#include "Move.h"
#include "Position.h"
#include "SearchNode.h"
#include "Variation.h"

namespace chesslib {

class SearchTree {
public:
    SearchTree(const Position& initialPosition);

    SearchNode& getRoot()
    {
        return root_;
    }

    Variation getBestVariation() const
    {
        return Variation(root_.getBestChild());
    }

private:
    Position initialPosition_;
    SearchNode root_;
};

}
