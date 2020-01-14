#pragma once

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
        int depthPly,
        evaluation_t alpha,
        evaluation_t beta);

    evaluation_t runQuiescentSearch(
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
