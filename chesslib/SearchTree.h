#pragma once

#include "Move.h"
#include "Position.h"
#include "SearchNode.h"
#include "Variation.h"

#include <atomic>
#include <cstdint>

namespace chesslib {

class SearchTree {
public:
    SearchTree(const Position& initialPosition);

    SearchNode& getRoot()
    {
        return root_;
    }

    Variation getBestVariation() const
    {
        return Variation(root_.getBestChild());
    }

    uint64_t getNodeCount() const
    {
        return nodeCount_;
    }

    void createBestChildNode(SearchNode& parent, Move move, double evaluation);

    void insertAsBestChildNode(SearchNode& parent, SearchNodeRef nodeToInsert);

    SearchNodeRef createIsolatedNode(Move move)
    {
        return SearchNode::createRef(move);
    }

private:
    Position initialPosition_;
    SearchNode root_;
    std::atomic<uint64_t> nodeCount_;
};

}
