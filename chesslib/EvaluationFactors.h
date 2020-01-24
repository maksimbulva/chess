#pragma once

#include "PawnEvaluationFactors.h"
#include "pawns_evaluation.h"
#include "types.h"

#include <array>

namespace chesslib {

struct EvaluationFactors {
public:

public:
    EvaluationFactors() = default;

    evaluation_t getMaterial() const
    {
        return material_;
    }

    void setMaterial(const evaluation_t& material)
    {
        material_ = material;
    }

    evaluation_t getTableValue() const
    {
        return tableValue_;
    }

    void setTableValue(const evaluation_t& tableValue)
    {
        tableValue_ = tableValue;
    }

    const PawnEvaluationFactors& getPawnFactors() const
    {
        return pawnFactors_;
    }

    void setPawnFactors(const PawnEvaluationFactors& pawnFactors)
    {
        pawnFactors_ = pawnFactors;
    }

    void setPawnFactors(const bitboard_t& pawnsBitboard, player_t player)
    {
        updatePawnFactors(pawnsBitboard, player, pawnFactors_);
    }

private:
    evaluation_t material_;
    evaluation_t tableValue_;
    PawnEvaluationFactors pawnFactors_;
};

using EvaluationFactorsArray = std::array<EvaluationFactors, PLAYER_COUNT>;

}
