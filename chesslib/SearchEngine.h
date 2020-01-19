#pragma once

#include "EvaluationFactors.h"
#include "MemoryPool.h"
#include "Position.h"
#include "TranspositionTable.h"
#include "Variation.h"

#include <vector>

namespace chesslib {

class Evaluator;
struct PositionHash;
struct ScoredMove;

class SearchEngine
{
public:
    SearchEngine(Position position, Evaluator& evaluator);

    Variation runSearch(int depthPly);

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

    static PositionHash getChildHash(
        const PositionHash& parentHash,
        PositionFlags childPositionFlags,
        const ScoredMove& movePlayed);

    Position position_;
    Evaluator* const evaluator_;
    int searchDepthPly_;
    MemoryPool memoryPool_;
    TranspositionTable transpositionTable_;
};

}
