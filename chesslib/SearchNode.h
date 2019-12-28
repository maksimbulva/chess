#pragma once

#include "Move.h"

#include <memory>

namespace chesslib {

class SearchNode;
using SearchNodeRef = std::shared_ptr<SearchNode>;

class SearchNode {
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

    const SearchNode* getChild() const
    {
        return child_.get();
    }

    const SearchNode* getSibling() const
    {
        return sibling_.get();
    }

    SearchNodeRef addChild(Move move)
    {
        auto child = createRef(move, child_);
        child_ = child;
        return child;
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
