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

private:
    Evaluator evaluator_;
    uint64_t maxEvaluations_;
};

}
