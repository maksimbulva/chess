#pragma once

#include "types.h"
#include "Variation.h"

#include <cstdint>

namespace chesslib {

struct SearchInfo {
    SearchInfo()
        : searchTreeSize(0)
        , evaluatedPositionCount(0)
        , searchTimeMs(0)
        , playerToMove(Black)
    {
    }

    uint64_t getNodesPerSecond() const
    {
        return searchTimeMs >= 1000 ? evaluatedPositionCount * 1000 / searchTimeMs : evaluatedPositionCount;
    }

    double getEvaluation() const
    {
        const double evaluationSideMultiplier = playerToMove == Black ? -1.0 : 1.0;
        return evaluationSideMultiplier * bestVariation.getEvaluation();
    }

    int64_t getEvaluationInCentipawns() const
    {
        return static_cast<int64_t>(getEvaluation() * 100.0);
    }

    Variation bestVariation;
    uint64_t searchTreeSize;
    uint64_t evaluatedPositionCount;
    int64_t searchTimeMs;
    player_t playerToMove;
};

}
