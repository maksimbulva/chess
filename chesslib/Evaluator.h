#pragma once

#include "EvaluationFactors.h"
#include "Move.h"
#include "types.h"
#include "ZobristHasher.h"

#include <array>
#include <atomic>

namespace chesslib {

class Position;

class Evaluator {
public:
    Evaluator();

    uint64_t getEvaluatedPositionCount() const
    {
        return evaluatedPositionCount_;
    }

    EvaluationFactors getEvaluationFactors(const Position& position, player_t player) const;

    void calculateChildEvaluationFactors(
        EvaluationFactorsArray& childFactors,
        position_hash_t& childPositionHash,
        const EvaluationFactorsArray& parentFactors,
        const Move movePlayed,
        const player_t player) const;

    evaluation_t evaluate(const EvaluationFactorsArray& factors);

    evaluation_t evaluateNoLegalMovesPosition(Position& position);

    position_hash_t getPositionHash(const Position& position) const;

    static evaluation_t getSideMultiplier(player_t playerToMove)
    {
        return playerToMove == Black ? -1 : 1;
    }

    static evaluation_t getMaterialGain(Move move);

    static evaluation_t getTableValueDelta(Move move, player_t playerToMove);

private:
    // TODO: consider using std::atomic_uint64_t
    std::atomic<uint64_t> evaluatedPositionCount_;
    ZobristHasher hasher_;
    std::array<position_hash_t, PLAYER_COUNT> shortCastleRookHash_;
    std::array<position_hash_t, PLAYER_COUNT> longCastleRookHash_;
};

}
