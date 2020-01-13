#pragma once

#include "types.h"
#include "Variation.h"

#include <cstdint>

namespace chesslib {

struct SearchInfo {
    SearchInfo()
        : evaluatedPositionCount(0)
        , searchTimeMs(0)
        , playerToMove(Black)
    {
    }

    uint64_t getNodesPerSecond() const
    {
        return searchTimeMs >= 1000 ? evaluatedPositionCount * 1000 / searchTimeMs : evaluatedPositionCount;
    }

    evaluation_t getEvaluation() const
    {
        const evaluation_t evaluationSideMultiplier = playerToMove == Black ? -1 : 1;
        return evaluationSideMultiplier * bestVariation.getEvaluation();
    }

    int64_t getEvaluationInCentipawns() const
    {
        return getEvaluation();
    }

    Variation bestVariation;
    uint64_t evaluatedPositionCount;
    int64_t searchTimeMs;
    player_t playerToMove;
};

}
