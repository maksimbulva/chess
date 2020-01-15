#pragma once

#include "types.h"

#include <array>

namespace chesslib {

struct EvaluationFactors {
public:
    EvaluationFactors()
        : material_(0)
        , tableValue_(0)
    {
    }

    EvaluationFactors(evaluation_t material, evaluation_t tableValue)
        : material_(material)
        , tableValue_(tableValue)
    {
    }

    evaluation_t getMaterial() const
    {
        return material_;
    }

    evaluation_t getTableValue() const
    {
        return tableValue_;
    }

private:
    evaluation_t material_;
    evaluation_t tableValue_;
};

using EvaluationFactorsArray = std::array<EvaluationFactors, PLAYER_COUNT>;

}
