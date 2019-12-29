#pragma once

#include "Move.h"

#include <memory>

namespace chesslib {

class SearchNode;
using SearchNodeRef = std::shared_ptr<SearchNode>;

class SearchNode {
    friend class SearchTree;
public:
    SearchNode(Move move)
        : move_(move)
        , isEvaluated_(false)
        , evaluation_(0.0)
    {
    }

    SearchNode(Move move, SearchNodeRef sibling)
        : move_(move)
        , isEvaluated_(false)
        , evaluation_(0.0)
        , sibling_(std::move(sibling))
    {
    }

    Move getMove() const
    {
        return move_;
    }

    const SearchNode* const getBestChild() const
    {
        return child_.get();
    }

    bool hasChildren() const
    {
        return child_ != nullptr;
    }

    bool isEvaluated() const
    {
        return isEvaluated_;
    }

    double getEvaluation() const
    {
        return evaluation_;
    }

    void setEvaluation(double value)
    {
        isEvaluated_ = true;
        evaluation_ = value;
    }

private:
    static SearchNodeRef createRef(Move move, SearchNodeRef sibling)
    {
        return std::make_shared<SearchNode>(move, std::move(sibling));
    }

private:
    Move move_;
    bool isEvaluated_;
    double evaluation_;
    SearchNodeRef child_;
    SearchNodeRef sibling_;
};

}
