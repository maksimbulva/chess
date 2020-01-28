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
class Player;
struct PositionHash;
struct ScoredMove;

class SearchEngine
{
public:
    SearchEngine(Position position, const Player& player);

    Variation runSearch(int depthPly);

    bool isSearchAborted() const
    {
        return isSearchAborted_;
    }

    uint64_t getEvaluatedPositionCount() const
    {
        return evaluatedPositionCount_;
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

    int getCurrentSearchDepthPly(int remainingDepthPly) const;

    void abortSearchIfNeeded();

    evaluation_t evaluate(const EvaluationFactorsArray& factors);
    evaluation_t evaluateNoLegalMovesPosition(int currentSearchDepthPly);

    static PositionHash getChildHash(
        const PositionHash& parentHash,
        PositionFlags childPositionFlags,
        const ScoredMove& movePlayed);

    Position position_;
    const Evaluator* const evaluator_;
    const uint64_t maxEvaluations_;
    int searchDepthPly_;
    MemoryPool memoryPool_;
    TranspositionTable transpositionTable_;

    std::atomic_bool isSearchAborted_;
    std::atomic_uint64_t evaluatedPositionCount_;
};

}
