#include "SearchTree.h"

#include "evaluate.h"

#include <limits>

namespace chesslib {

SearchTree::SearchTree(const Position& initialPosition)
    : initialPosition_(initialPosition)
    , root_(Move::NullMove())
    , nodeCount_(1)
{
}

void SearchTree::createBestChildNode(SearchNode& parent, Move move)
{
    auto oldFirstChild = std::move(parent.child_);
    auto newChild = SearchNode::createRef(move, std::move(oldFirstChild));
    parent.child_ = std::move(newChild);
    nodeCount_++;
}

void SearchTree::createSuboptimalChildNode(SearchNode& parent, Move move)
{
    auto& firstChild = parent.child_;
    if (firstChild != nullptr) {
        auto oldSecondChild = std::move(firstChild->sibling_);
        auto newChild = SearchNode::createRef(move, std::move(oldSecondChild));
        firstChild->sibling_ = std::move(newChild);
        nodeCount_++;
    }
    else {
        createBestChildNode(parent, move);
    }
}


}
