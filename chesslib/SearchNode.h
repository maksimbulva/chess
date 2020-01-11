#pragma once

#include "Move.h"

#include <memory>

namespace chesslib {

class SearchNode;
using SearchNodeRef = std::shared_ptr<SearchNode>;

class SearchNode {
    friend class SearchTree;
public:
    // TODO: probably, constuctors can be private
    SearchNode(Move move)
        : move_(move)
        , evaluation_(0)
    {
    }

    SearchNode(Move move, evaluation_t evaluation)
        : move_(move)
        , evaluation_(evaluation)
    {
    }

    static SearchNodeRef createRef(Move move)
    {
        return std::make_shared<SearchNode>(move);
    }

    static SearchNodeRef createRef(Move move, evaluation_t evaluation)
    {
        return std::make_shared<SearchNode>(move, evaluation);
    }

    Move getMove() const
    {
        return move_;
    }

    const SearchNode* const getBestChild() const
    {
        return child_.get();
    }

    void setChild(SearchNodeRef child)
    {
        child_ = std::move(child);
    }

    evaluation_t getEvaluation() const
    {
        return evaluation_;
    }

    void setEvaluation(evaluation_t value)
    {
        evaluation_ = value;
    }

private:
    Move move_;
    evaluation_t evaluation_;
    SearchNodeRef child_;
};

}
