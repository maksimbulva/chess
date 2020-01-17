#include "Evaluator.h"

#include "Position.h"
#include "squares.h"

#include <array>

namespace chesslib {

namespace {

constexpr evaluation_t CheckmateValue = 100000;
constexpr evaluation_t StalemateValue = 0;

static std::array<evaluation_t, King + 1> MATERIAL_VALUE = {
    /* NoPiece */ 0,
    /* Pawn*/     100,
    /* Knight */  300,
    /* Bishop */  300,
    /* Rook */    500,
    /* Queen */   900,
    /* King */    0
};

using TableValues = std::array<evaluation_t, SQUARE_COUNT>;

// For evaluation based on https://www.chessprogramming.org/Simplified_Evaluation_Function
TableValues TABLE_PAWN_VALUES = {
    0, 0, 0, 0, 0, 0, 0, 0,
    50, 50, 50, 50, 50, 50, 50, 50,
    10, 10, 20, 30, 30, 20, 10, 10,
    5, 5, 10, 25, 25, 10, 5, 5,
    0, 0, 0, 20, 20, 0, 0, 0,
    5, -5, -10, 0, 0, -10, -5, 5,
    5, 10, 10, -20, -20, 10, 10, 5,
    0, 0, 0, 0, 0, 0, 0, 0
};

TableValues TABLE_KNIGHT_VALUES = {
    -50, -40, -30, -30, -30, -30, -40, -50,
    -40, -20, 0, 0, 0, 0, -20, -40,
    -30, 0, 10, 15, 15, 10, 0, -30,
    -30, 5, 15, 20, 20, 15, 5, -30,
    -30, 0, 15, 20, 20, 15, 0, -30,
    -30, 5, 10, 15, 15, 10, 5, -30,
    -40, -20, 0, 5, 5, 0, -20, -40,
    -50, -40, -30, -30, -30, -30, -40, -50
};

TableValues TABLE_BISHOP_VALUES = {
    -20, -10, -10, -10, -10, -10, -10, -20,
    -10, 0, 0, 0, 0, 0, 0, -10,
    -10, 0, 5, 10, 10, 5, 0, -10,
    -10, 5, 5, 10, 10, 5, 5, -10,
    -10, 0, 10, 10, 10, 10, 0, -10,
    -10, 10, 10, 10, 10, 10, 10, -10,
    -10, 5, 0, 0, 0, 0, 5, -10,
    -20, -10, -10, -10, -10, -10, -10, -20
};

TableValues TABLE_ROOK_VALUES = {
    0, 0, 0, 0, 0, 0, 0, 0,
    5, 10, 10, 10, 10, 10, 10, 5,
    -5, 0, 0, 0, 0, 0, 0, -5,
    -5, 0, 0, 0, 0, 0, 0, -5,
    -5, 0, 0, 0, 0, 0, 0, -5,
    -5, 0, 0, 0, 0, 0, 0, -5,
    -5, 0, 0, 0, 0, 0, 0, -5,
    0, 0, 0, 5, 5, 0, 0, 0
};

TableValues TABLE_QUEEN_VALUES = {
    -20, -10, -10, -5, -5, -10, -10, -20,
    -10, 0, 0, 0, 0, 0, 0, -10,
    -10, 0, 5, 5, 5, 5, 0, -10,
    -5, 0, 5, 5, 5, 5, 0, -5,
    0, 0, 5, 5, 5, 5, 0, -5,
    -10, 5, 5, 5, 5, 5, 0, -10,
    -10, 0, 5, 0, 0, 0, 0, -10,
    -20, -10, -10, -5, -5, -10, -10, -20
};

TableValues TABLE_KING_VALUES = {
    -30, -40, -40, -50, -50, -40, -40, -30,
    -30, -40, -40, -50, -50, -40, -40, -30,
    -30, -40, -40, -50, -50, -40, -40, -30,
    -30, -40, -40, -50, -50, -40, -40, -30,
    -20, -30, -30, -40, -40, -30, -30, -20,
    -10, -20, -20, -20, -20, -20, -20, -10,
    20, 20, 0, 0, 0, 0, 20, 20,
    20, 30, 10, 0, 0, 10, 30, 20
};

// TODO: For endgame or midgame we can consider another king table value

}  // namespace

const std::array<TableValues*, King + 1> Evaluator::TABLE_VALUES = {
    /* NoPiece */ nullptr,
    &TABLE_PAWN_VALUES,
    &TABLE_KNIGHT_VALUES,
    &TABLE_BISHOP_VALUES,
    &TABLE_ROOK_VALUES,
    &TABLE_QUEEN_VALUES,
    &TABLE_KING_VALUES
};

Evaluator::Evaluator()
    : evaluatedPositionCount_(0)
{
}

evaluation_t Evaluator::getMaterialValue(piece_type_t pieceType) const
{
    return MATERIAL_VALUE[pieceType];
}

EvaluationFactors Evaluator::getEvaluationFactors(const Position& position, player_t player) const
{
    return EvaluationFactors(
        evaluateMaterial(position, player),
        evaluateTableValues(position, player));
}

void Evaluator::calculateChildEvaluationFactors(
    EvaluationFactorsArray& childFactors,
    const EvaluationFactorsArray& parentFactors,
    const ScoredMove& movePlayed,
    const player_t player) const
{
    const auto& myParent = parentFactors[player];
    const auto& otherParent = parentFactors[getOtherPlayer(player)];

    childFactors[player] = EvaluationFactors(
        myParent.getMaterial() + movePlayed.getMyMaterialGain(),
        myParent.getTableValue() + movePlayed.getMyTableValueGain());

    childFactors[getOtherPlayer(player)] = EvaluationFactors(
        otherParent.getMaterial() + movePlayed.getTheirMaterialGain(),
        otherParent.getTableValue() + movePlayed.getTheirTableValueGain());
}

// TODO: this is temporary method. Replace with some ML evaluations
evaluation_t Evaluator::evaluate(const EvaluationFactorsArray& factors)
{
    ++evaluatedPositionCount_;
    return factors[White].getMaterial() + factors[White].getTableValue()
        - factors[Black].getMaterial() - factors[Black].getTableValue();
}

evaluation_t Evaluator::evaluateNoLegalMovesPosition(Position& position)
{
    ++evaluatedPositionCount_;
    // Either we are checkmated or it is a stalemate
    return position.isInCheck() ? -CheckmateValue : StalemateValue;
}

evaluation_t Evaluator::evaluateMaterial(const Position& position, player_t player) const
{
    evaluation_t result = 0;
    auto piecesIt = position.getBoard().getPieceIterator(player);
    while (true) {
        result += MATERIAL_VALUE[piecesIt.getPieceType()];
        if (piecesIt.hasNext()) {
            ++piecesIt;
        }
        else {
            break;
        }
    }
    return result;
}

evaluation_t Evaluator::evaluateTableValues(const Position& position, player_t player)
{
    evaluation_t result = 0;
    position.getBoard().doForEachPiece(player, [&result, player] (piece_type_t pieceType, square_t square)
        {
            result += getTableValue(pieceType, player, square);
        });
    return result;
}

}
