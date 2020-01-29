#pragma once

#include "Evaluator.h"

#include <cstdint>

namespace chesslib {

class Player
{
public:
    Player();

    const Evaluator& getEvaluator() const
    {
        return evaluator_;
    }

    uint64_t getMaxEvaluations() const
    {
        return maxEvaluations_;
    }

    void setMaxEvaluations(uint64_t maxEvaluations);

    int getEvaluationRandomness() const
    {
        return evaluationRandomness_;
    }

private:
    Evaluator evaluator_;
    uint64_t maxEvaluations_;
    // Must be zero or a power of two
    evaluation_t evaluationRandomness_;
};

}
