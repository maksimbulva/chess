#include "Evaluator.h"

#include "bitboard.h"
#include "pawns_evaluation.h"
#include "Position.h"
#include "squares.h"

namespace chesslib {

namespace {

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
    : materialValue_({
        /* NoPiece */ 0,
        /* Pawn */    100,
        /* Knight */  300,
        /* Bishop */  300,
        /* Rook */    500,
        /* Queen */   900,
        /* King */    0
    })
{
}

void Evaluator::setMaterialValue(piece_type_t pieceType, evaluation_t materialValue)
{
    materialValue_[pieceType] = materialValue;
}

EvaluationFactors Evaluator::getEvaluationFactors(const Position& position, player_t player) const
{
    EvaluationFactors factors;
    factors.setMaterial(evaluateMaterial(position, player));
    factors.setTableValue(evaluateTableValues(position, player));
    factors.setPawnFactors(getPawnsBitboard(position.getBoard(), player), player);
    return factors;
}

void Evaluator::calculateChildEvaluationFactors(
    EvaluationFactorsArray& childFactors,
    const EvaluationFactorsArray& parentFactors,
    const ScoredMove& movePlayed,
    const player_t player) const
{
    const auto& myParent = parentFactors[player];
    const auto& otherParent = parentFactors[getOtherPlayer(player)];

    const Move& move = movePlayed.getMove();

    bool myPawnsChanged = false;
    bitboard_t myPawnsBitboard = myParent.getPawnFactors().pawnsBitboard;
    if (move.getPieceType() == Pawn) {
        unsetSquare(myPawnsBitboard, move.getOriginSquare());
        if (!move.isPromotion()) {
            setSquare(myPawnsBitboard, move.getDestSquare());
        }
        myPawnsChanged = true;
    }

    bool otherPawnsChanged = false;
    bitboard_t otherPawnsBitboard = otherParent.getPawnFactors().pawnsBitboard;
    if (move.getCapturedPieceType() == Pawn) {
        unsetSquare(otherPawnsBitboard, move.getCapturedPieceSquare());
        otherPawnsChanged = true;
    }

    auto& myChildFactors = childFactors[player];
    myChildFactors.setMaterial(myParent.getMaterial() + movePlayed.getMyMaterialGain());
    myChildFactors.setTableValue(myParent.getTableValue() + movePlayed.getMyTableValueGain());

    if (myPawnsChanged) {
        myChildFactors.setPawnFactors(myPawnsBitboard, player);
    }
    else {
        myChildFactors.setPawnFactors(myParent.getPawnFactors());
    }

    auto& otherChildFactors = childFactors[getOtherPlayer(player)];
    otherChildFactors.setMaterial(otherParent.getMaterial() + movePlayed.getTheirMaterialGain());
    otherChildFactors.setTableValue(otherParent.getTableValue() + movePlayed.getTheirTableValueGain());

    if (otherPawnsChanged) {
        otherChildFactors.setPawnFactors(otherPawnsBitboard, getOtherPlayer(player));
    }
    else {
        otherChildFactors.setPawnFactors(otherParent.getPawnFactors());
    }
}

// TODO: this is temporary method. Replace with some ML evaluations
evaluation_t Evaluator::evaluate(const EvaluationFactorsArray& factors) const
{
    return factors[White].getMaterial() + factors[White].getTableValue()
        - factors[Black].getMaterial() - factors[Black].getTableValue();
}

evaluation_t Evaluator::evaluateNoLegalMovesPosition(Position& position, int currentSearchDepthPly) const
{
    // Either we are checkmated or it is a stalemate
    return position.isInCheck() ? currentSearchDepthPly - CheckmateValue : StalemateValue;
}

evaluation_t Evaluator::evaluateMaterial(const Position& position, player_t player) const
{
    evaluation_t material = 0;
    position.getBoard().doForEachPiece(player, [this, &material](piece_type_t pieceType, square_t)
        {
            material += getMaterialValue(pieceType);
        });
    return material;
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
