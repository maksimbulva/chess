#pragma once

#include "Move.h"
#include "types.h"

namespace chesslib {

struct ScoredMove {
    friend class MovesCollection;
public:
    ScoredMove()
        : score_(0)
    {
    }

    Move getMove() const
    {
        return move_;
    }

    evaluation_t getScore() const
    {
        return score_;
    }

private:
    Move move_;
    evaluation_t score_;
};

}
