#pragma once

#include "Move.h"
#include "MovesCollection.h"

#include <vector>

namespace chesslib {

class SearchNode;

class Variation
{
public:
    Variation()
        : evaluation_(0)
    {
    }

    Variation(evaluation_t evaluation, const MovesCollection& moves);

    evaluation_t getEvaluation() const
    {
        return evaluation_;
    }

    const std::vector<Move>& getMoves() const
    {
        return moves_;
    }

private:
    evaluation_t evaluation_;
    std::vector<Move> moves_;
};

}
