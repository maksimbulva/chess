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
    double runAlphaBetaSearch(
        Position& position,
        SearchNode& parent,
        int depthPly,
        double alpha,
        double beta);

private:
    Evaluator* evaluator_;
};

}
