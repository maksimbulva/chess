#pragma once

#include "Move.h"

#include <vector>

namespace chesslib {

class SearchNode;

class Variation
{
    friend class SearchTree;
public:
    Variation()
        : evaluation_(0.0)
    {
    }

    const std::vector<Move>& getMoves() const
    {
        return moves_;
    }

private:
    Variation(const SearchNode* startingNode);

private:
    double evaluation_;
    std::vector<Move> moves_;
};

}
