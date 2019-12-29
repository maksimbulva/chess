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

void SearchTree::createBestChildNode(SearchNode& parent, Move move, double evaluation)
{
    auto oldFirstChild = std::move(parent.child_);
    auto newChild = SearchNode::createRef(move, std::move(oldFirstChild), evaluation);
    parent.child_ = std::move(newChild);
    ++nodeCount_;
}

void SearchTree::insertAsBestChildNode(SearchNode& parent, SearchNodeRef nodeToInsert)
{
    auto oldFirstChild = std::move(parent.child_);
    parent.child_ = std::move(nodeToInsert);
    ++nodeCount_;
}

}
