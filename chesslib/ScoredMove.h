#pragma once

#include "Move.h"
#include "types.h"

namespace chesslib {

class Evaluator;

struct ScoredMove {
public:
    ScoredMove();

    void updateScore(const Evaluator& evaluator, player_t player);

    Move getMove() const
    {
        return move_;
    }

    void setMove(Move move)
    {
        move_ = move;
    }

    evaluation_t getScore() const
    {
        return score_;
    }

    void setScore(evaluation_t score)
    {
        score_ = score;
    }

    evaluation_t getMyMaterialGain() const
    {
        return myMaterialGain_;
    }

    evaluation_t getTheirMaterialGain() const
    {
        return theirMaterialGain_;
    }

    evaluation_t getMyTableValueGain() const
    {
        return myTableValueGain_;
    }

    evaluation_t getTheirTableValueGain() const
    {
        return theirTableValueGain_;
    }

    position_hash_t getHash() const
    {
        return hash_;
    }

private:
    Move move_;
    evaluation_t score_;
    evaluation_t myMaterialGain_;
    evaluation_t theirMaterialGain_;
    evaluation_t myTableValueGain_;
    evaluation_t theirTableValueGain_;
    position_hash_t hash_;
};

}
