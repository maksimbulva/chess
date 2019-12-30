#pragma once

#include <atomic>
#include <cstdint>

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

    double evaluate(const Position& position);

    double evaluateNoLegalMovesPosition(Position& position);

private:
    // TODO: consider using std::atomic_uint64_t
    std::atomic<uint64_t> evaluatedPositionCount_;
};

}
