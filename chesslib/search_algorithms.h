#pragma once

namespace chesslib {

class Position;
class SearchNode;
class SearchTree;

double runNegatedMinMax(
    SearchNode& startingNode,
    SearchTree& searchTree,
    const Position& startingPosition);

}
