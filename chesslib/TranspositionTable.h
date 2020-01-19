#pragma once

#include "Move.h"
#include "types.h"

#include <unordered_map>

namespace chesslib {

class TranspositionTable {
public:
    enum EvaluationConstraint : fastint {
        Exact,
        AtMost,
        AtLeast
    };

    struct Value {
    public:
        Value()
        {            
        }

        explicit constexpr Value(uint64_t encoded)
            : encoded_(encoded)
        {
        }

        Value(
            Move bestMove,
            int depthPly,
            evaluation_t evaluation,
            EvaluationConstraint evaluationConstraint)
            : encoded_(
                static_cast<uint64_t>(bestMove.getEncodedValue()) |
                (static_cast<uint64_t>(depthPly) << 32) |
                ((static_cast<uint64_t>(evaluation) + 0x8000) << 41) |
                (static_cast<uint64_t>(evaluationConstraint) << 57))
        {
        }

        bool isEmpty() const
        {
            return encoded_ == 0;
        }

        bool isNotEmpty() const
        {
            return encoded_ != 0;
        }

        Move getBestMove() const
        {
            return Move(static_cast<encoded_move_t>(encoded_ & 0xFFFFFFFFULL));
        }

        int getDepthPly() const
        {
            return static_cast<int>(encoded_ >> 32) & 0xFF;
        }

        evaluation_t getEvaluation() const
        {
            return (static_cast<evaluation_t>(encoded_ >> 41) & 0xFFFF) - 0x8000;
        }

        EvaluationConstraint getEvaluationConstraint() const
        {
            return static_cast<EvaluationConstraint>(
                static_cast<fastint>(encoded_ >> 57) & static_cast<fastint>(3));
        }

    private:
        uint64_t encoded_;
    };

public:
    TranspositionTable();

    void insertBetaCutoff(position_hash_t key, Move move, int depthPly, evaluation_t beta)
    {
        if (isNotFull()) {
            table_[key] = Value(move, depthPly, beta, AtLeast);
        }
    }

    void insertNoAlphaImprovement(position_hash_t key, int depthPly, evaluation_t alpha)
    {
        if (isNotFull()) {
            table_[key] = Value(Move::NullMove(), depthPly, alpha, AtMost);
        }
    }

    void insertExactEvaluation(position_hash_t key, Move move, int depthPly, evaluation_t evaluation)
    {
        if (isNotFull()) {
            table_[key] = Value(move, depthPly, evaluation, Exact);
        }
    }

    Value findValue(position_hash_t key) const
    {
        auto it = table_.find(key);
        if (it != table_.end()) {
            return it->second;
        }
        return Value(0);
    }

    bool isNotFull() const
    {
        return table_.size() < MAX_ELEMENT_COUNT;
    }

private:
    static constexpr size_t MAX_ELEMENT_COUNT = 1 << 20;

    std::unordered_map<position_hash_t, Value> table_;
};

}
