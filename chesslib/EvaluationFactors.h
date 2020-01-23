#pragma once

#include "bitboard.h"
#include "types.h"

#include <array>

namespace chesslib {

struct EvaluationFactors {
public:
    EvaluationFactors() = default;

    EvaluationFactors(
        evaluation_t material,
        evaluation_t tableValue,
        bitboard_t pawnsBitboard,
        uint32_t pawnsColumnMask)
        : material_(material)
        , tableValue_(tableValue)
        , pawnsBitboard_(pawnsBitboard)
        , pawnsColumnMask_(pawnsColumnMask)
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

    uint64_t getPawnsBitboard() const
    {
        return pawnsBitboard_;
    }

    uint32_t getPawnsColumnMask() const
    {
        return pawnsColumnMask_;
    }

private:
    evaluation_t material_;
    evaluation_t tableValue_;
    bitboard_t pawnsBitboard_;
    uint32_t pawnsColumnMask_;
};

using EvaluationFactorsArray = std::array<EvaluationFactors, PLAYER_COUNT>;

}
