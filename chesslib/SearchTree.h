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

    void createBestChildNode(SearchNode& parent, Move move);

    void createSuboptimalChildNode(SearchNode& parent, Move move);

private:
    Position initialPosition_;
    SearchNode root_;
    std::atomic<uint64_t> nodeCount_;
};

}
