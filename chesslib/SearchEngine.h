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
    SearchEngine(Position position, Evaluator& evaluator);

    Variation runSearch(int depthPly);

private:
    evaluation_t runAlphaBetaSearch(
        SearchNode& parent,
        int depthPly,
        evaluation_t alpha,
        evaluation_t beta);

    evaluation_t runQuiescentSearch(
        evaluation_t alpha,
        evaluation_t beta);

private:
    Position position_;
    Evaluator* const evaluator_;
};

}
