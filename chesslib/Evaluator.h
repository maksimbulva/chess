#pragma once

#include "types.h"

#include <atomic>

namespace chesslib {

class Position;

class Evaluator {
public:
    Evaluator()
        : evaluatedPositionCount_(0)
    {
    }

    uint64_t getEvaluatedPositionCount() const
    {
        return evaluatedPositionCount_;
    }

    evaluation_t evaluate(const Position& position);

    evaluation_t evaluateNoLegalMovesPosition(Position& position);

    static evaluation_t getSideMultiplier(player_t playerToMove)
    {
        return playerToMove == Black ? -1 : 1;
    }

private:
    // TODO: consider using std::atomic_uint64_t
    std::atomic<uint64_t> evaluatedPositionCount_;
};

}
