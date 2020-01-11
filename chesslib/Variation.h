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
        : evaluation_(0)
    {
    }

    Variation(evaluation_t evaluation, std::vector<Move> moves)
        : evaluation_(evaluation)
        , moves_(std::move(moves))
    {
    }

    evaluation_t getEvaluation() const
    {
        return evaluation_;
    }

    const std::vector<Move>& getMoves() const
    {
        return moves_;
    }

private:
    Variation(const SearchNode* startingNode);

private:
    evaluation_t evaluation_;
    std::vector<Move> moves_;
};

}
