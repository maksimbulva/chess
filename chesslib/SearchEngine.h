#pragma once

#include "Position.h"
#include "Variation.h"

#include <vector>

namespace chesslib {

class Evaluator;
class SearchNode;

class SearchEngine
{
public:
    Variation runSearch(Position position, Evaluator& evaluator, int depthPly);

private:
    // TODO: move position to object field
    evaluation_t runAlphaBetaSearch(
        Position& position,
        SearchNode& parent,
        int depthPly,
        evaluation_t alpha,
        evaluation_t beta);

private:
    Evaluator* evaluator_;
};

}
