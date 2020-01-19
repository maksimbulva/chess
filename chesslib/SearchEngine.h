#pragma once

#include "EvaluationFactors.h"
#include "MemoryPool.h"
#include "Position.h"
#include "TranspositionTable.h"
#include "Variation.h"

#include <atomic>
#include <vector>

namespace chesslib {

class Evaluator;
struct PositionHash;
struct ScoredMove;

class SearchEngine
{
public:
    SearchEngine(Position position, Evaluator& evaluator, uint64_t maxEvaluations);

    Variation runSearch(int depthPly);

    bool isSearchAborted() const
    {
        return isSearchAborted_;
    }

private:
    evaluation_t runAlphaBetaSearch(
        const EvaluationFactorsArray& parentEvaluationFactors,
        const PositionHash& parentHash,
        int depthPly,
        evaluation_t alpha,
        evaluation_t beta);

    evaluation_t runQuiescentSearch(
        const EvaluationFactorsArray& parentEvaluationFactors,
        const PositionHash& parentHash,
        evaluation_t alpha,
        evaluation_t beta);

private:
    MovesCollection getPrincipalVariation(PositionHash parentHash);

    void abortSearchIfNeeded();

    static PositionHash getChildHash(
        const PositionHash& parentHash,
        PositionFlags childPositionFlags,
        const ScoredMove& movePlayed);

    Position position_;
    Evaluator* const evaluator_;
    const uint64_t maxEvaluations_;
    int searchDepthPly_;
    MemoryPool memoryPool_;
    TranspositionTable transpositionTable_;
    std::atomic_bool isSearchAborted_;
};

}
