#pragma once

namespace chesslib {

class Evaluator;
class Position;
class SearchNode;
class SearchTree;

double runNegatedMinMax(
    SearchNode& startingNode,
    SearchTree& searchTree,
    const Position& startingPosition,
    Evaluator& evaluator,
    int depthPly);

}
