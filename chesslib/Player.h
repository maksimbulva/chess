#pragma once

#include "Evaluator.h"

#include <cstdint>

namespace chesslib {

class Player
{
public:
    Player();

    Evaluator& getEvaluator()
    {
        return evaluator_;
    }

    uint64_t getMaxEvaluations() const
    {
        return maxEvaluations_;
    }

private:
    Evaluator evaluator_;
    uint64_t maxEvaluations_;
};

}
