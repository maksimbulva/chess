#pragma once

#include "EvaluationFactors.h"
#include "ScoredMove.h"
#include "types.h"

#include <array>

namespace chesslib {

class Position;

class Evaluator {
public:
    Evaluator();

    evaluation_t getMaterialValue(piece_type_t pieceType) const
    {
        return materialValue_[pieceType];
    }

    void setMaterialValue(piece_type_t pieceType, evaluation_t materialValue);

    EvaluationFactors getEvaluationFactors(const Position& position, player_t player) const;

    void calculateChildEvaluationFactors(
        EvaluationFactorsArray& childFactors,
        const EvaluationFactorsArray& parentFactors,
        const ScoredMove& movePlayed,
        const player_t player) const;

    evaluation_t evaluate(const EvaluationFactorsArray& factors) const;

    evaluation_t evaluateNoLegalMovesPosition(Position& position, int currentSearchDepthPly) const;

    evaluation_t evaluateMaterial(const Position& position, player_t player) const;

    static evaluation_t getSideMultiplier(player_t playerToMove)
    {
        return playerToMove == Black ? -1 : 1;
    }

    static evaluation_t getTableValue(piece_type_t pieceType, player_t player, square_t square)
    {
        const square_t adjustedSquare = player == White
            ? encodeSquare(MAX_ROW - getRow(square), getColumn(square))
            : square;
        return (*TABLE_VALUES[pieceType])[adjustedSquare];
    }

    static evaluation_t getTableValue(piece_type_t pieceType, square_t adjustedSquare)
    {
        return (*TABLE_VALUES[pieceType])[adjustedSquare];
    }

    static evaluation_t evaluateTableValues(const Position& position, player_t player);

public:
    static constexpr evaluation_t CheckmateValue = 100000;
    static constexpr evaluation_t StalemateValue = 0;
    static constexpr evaluation_t GoodEnoughToStopIterativeDeepening = CheckmateValue - 1000;

private:
    std::array<evaluation_t, King + 1> materialValue_;

    using TableValues = std::array<evaluation_t, SQUARE_COUNT>;
    static const std::array<TableValues*, King + 1> TABLE_VALUES;
};

}
