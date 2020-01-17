#pragma once

#include "EvaluationFactors.h"
#include "MemoryPool.h"
#include "Position.h"
#include "Variation.h"

#include <vector>

namespace chesslib {

class Evaluator;

class SearchEngine
{
public:
    SearchEngine(Position position, Evaluator& evaluator);

    Variation runSearch(int depthPly);

private:
    evaluation_t runAlphaBetaSearch(
        MovesCollection& bestMovesSequence,
        const EvaluationFactorsArray& parentEvaluationFactors,
        position_hash_t parentHash,
        int depthPly,
        evaluation_t alpha,
        evaluation_t beta);

    evaluation_t runQuiescentSearch(
        const EvaluationFactorsArray& parentEvaluationFactors,
        position_hash_t parentHash,
        evaluation_t alpha,
        evaluation_t beta);

private:
    Position position_;
    Evaluator* const evaluator_;
    int searchDepthPly_;
    MovesCollection bestMovesSequence_;
    MemoryPool memoryPool_;
};

}
