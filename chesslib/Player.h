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
    
    // The value must be zero or a power of two
    void setEvaluationRandomness(evaluation_t evaluationRandomness);

    void setMaterialValue(piece_type_t pieceType, evaluation_t materialValue);

private:
    Evaluator evaluator_;
    uint64_t maxEvaluations_;
    // Must be zero or a power of two
    evaluation_t evaluationRandomness_;
};

}
