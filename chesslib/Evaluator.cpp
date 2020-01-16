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

evaluation_t evaluateMaterial(const Position& position, player_t player)
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

std::array<TableValues*, King + 1> TABLE_VALUES = {
    /* NoPiece */ nullptr,
    &TABLE_PAWN_VALUES,
    &TABLE_KNIGHT_VALUES,
    &TABLE_BISHOP_VALUES,
    &TABLE_ROOK_VALUES,
    &TABLE_QUEEN_VALUES,
    &TABLE_KING_VALUES
};

const evaluation_t ShortCastleRookTableDelta =
    TABLE_ROOK_VALUES[encodeSquare(ROW_1, COLUMN_F)] - TABLE_ROOK_VALUES[encodeSquare(ROW_1, COLUMN_H)];

const evaluation_t LongCastleRookTableDelta =
    TABLE_ROOK_VALUES[encodeSquare(ROW_1, COLUMN_D)] - TABLE_ROOK_VALUES[encodeSquare(ROW_1, COLUMN_A)];

evaluation_t getTableValue(piece_type_t pieceType, square_t adjustedSquare)
{
    return (*TABLE_VALUES[pieceType])[adjustedSquare];
}

evaluation_t getTableValue(piece_type_t pieceType, player_t player, square_t square)
{
    const square_t adjustedSquare = player == White
        ? encodeSquare(MAX_ROW - getRow(square), getColumn(square))
        : square;
    return (*TABLE_VALUES[pieceType])[adjustedSquare];
}

evaluation_t evaluateTableValues(const Position& position, player_t player)
{
    evaluation_t result = 0;
    auto piecesIt = position.getBoard().getPieceIterator(player);
    while (true) {
        square_t square = piecesIt.getSquare();
        result += getTableValue(piecesIt.getPieceType(), player, square);
        if (piecesIt.hasNext()) {
            ++piecesIt;
        }
        else {
            break;
        }
    }
    return result;
}

}  // namespace

Evaluator::Evaluator()
    : evaluatedPositionCount_(0)
{
    shortCastleRookHash_[Black] = hasher_.getValue(Black, Squares::H8, Rook)
        ^ hasher_.getValue(Black, Squares::F8, Rook);
    shortCastleRookHash_[White] = hasher_.getValue(White, Squares::H1, Rook)
        ^ hasher_.getValue(White, Squares::F1, Rook);

    longCastleRookHash_[Black] = hasher_.getValue(Black, Squares::A8, Rook)
        ^ hasher_.getValue(Black, Squares::D8, Rook);
    longCastleRookHash_[White] = hasher_.getValue(White, Squares::A1, Rook)
        ^ hasher_.getValue(White, Squares::D1, Rook);
}

EvaluationFactors Evaluator::getEvaluationFactors(const Position& position, player_t player) const
{
    return EvaluationFactors(
        evaluateMaterial(position, player),
        evaluateTableValues(position, player));
}

void Evaluator::calculateChildEvaluationFactors(
    EvaluationFactorsArray& childFactors,
    position_hash_t& childPositionHash,
    const EvaluationFactorsArray& parentFactors,
    const Move movePlayed,
    const player_t player) const
{
    const auto& myParent = parentFactors[player];
    const auto& otherParent = parentFactors[getOtherPlayer(player)];

    evaluation_t myChildMaterial = myParent.getMaterial();
    evaluation_t myChildTableValue = myParent.getTableValue();
    evaluation_t otherChildMaterial = otherParent.getMaterial();
    evaluation_t otherChildTableValue = otherParent.getTableValue();

    const piece_type_t pieceType = movePlayed.getPieceType();
    const square_t originSquare = movePlayed.getOriginSquare();
    const square_t destSquare = movePlayed.getDestSquare();

    // Player lose value from piece's origin square
    myChildTableValue -= getTableValue(pieceType, player, originSquare);
    childPositionHash ^= hasher_.getValue(player, originSquare, pieceType);

    // Player gains value for piece's dest square
    if (movePlayed.isPromotion()) {
        const piece_type_t finalPieceType = movePlayed.getPromoteToPieceType();
        myChildTableValue += getTableValue(finalPieceType, player, destSquare);
        myChildMaterial += MATERIAL_VALUE[finalPieceType] - MATERIAL_VALUE[Pawn];
        childPositionHash ^= hasher_.getValue(player, destSquare, finalPieceType);
    }
    else {
        myChildTableValue += getTableValue(pieceType, player, destSquare);
        childPositionHash ^= hasher_.getValue(player, destSquare, pieceType);
    }

    if (movePlayed.isCapture()) {
        const piece_type_t capturedPieceType = movePlayed.getCapturedPieceType();
        square_t capturedSquare = movePlayed.isEnPassantCapture()
            ? encodeSquare(getRow(originSquare), getColumn(destSquare))
            : destSquare;
        otherChildTableValue -= getTableValue(capturedPieceType, getOtherPlayer(player), capturedSquare);
        otherChildMaterial -= MATERIAL_VALUE[capturedPieceType];
        childPositionHash ^= hasher_.getValue(getOtherPlayer(player), capturedSquare, capturedPieceType);
    }
    else if (movePlayed.isCastle()) {
        if (movePlayed.isShortCastle()) {
            myChildTableValue += ShortCastleRookTableDelta;
            childPositionHash ^= shortCastleRookHash_[player];
        }
        else {
            myChildTableValue += LongCastleRookTableDelta;
            childPositionHash ^= longCastleRookHash_[player];
        }
    }

    childFactors[player] = EvaluationFactors(myChildMaterial, myChildTableValue);
    childFactors[getOtherPlayer(player)] = EvaluationFactors(otherChildMaterial, otherChildTableValue);
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

evaluation_t Evaluator::getMaterialGain(Move move)
{
    evaluation_t result = 0;
    if (move.isCapture()) {
        result += MATERIAL_VALUE[move.getCapturedPieceType()];
    }
    if (move.isPromotion()) {
        result += MATERIAL_VALUE[move.getPromoteToPieceType()] - MATERIAL_VALUE[Pawn];
    }
    return result;
}

evaluation_t Evaluator::getTableValueDelta(Move move, player_t playerToMove)
{
    const piece_type_t pieceType = move.getPieceType();
    const square_t originSquare = move.getOriginSquare();
    const square_t destSquare = move.getDestSquare();

    // Player lose value from piece's origin square
    evaluation_t result = -getTableValue(pieceType, playerToMove, originSquare);

    // Player gains value for piece's dest square
    if (move.isPromotion()) {
        const piece_type_t finalPieceType = move.getPromoteToPieceType();
        result += getTableValue(finalPieceType, playerToMove, destSquare);
        result += MATERIAL_VALUE[move.getPromoteToPieceType()] - MATERIAL_VALUE[Pawn];
    }
    else {
        result += getTableValue(pieceType, playerToMove, destSquare);
    }

    if (move.isCapture()) {
        const piece_type_t capturedPieceType = move.getCapturedPieceType();
        const player_t otherPlayer = getOtherPlayer(playerToMove);
        square_t capturedSquare = move.isEnPassantCapture()
            ? encodeSquare(getRow(originSquare), getColumn(destSquare))
            : destSquare;
        result += getTableValue(capturedPieceType, otherPlayer, capturedSquare);
        result += MATERIAL_VALUE[capturedPieceType];
    }
    else if (move.isCastle()) {
        result += move.isShortCastle() ? ShortCastleRookTableDelta : LongCastleRookTableDelta;
    }

    return result;
}

position_hash_t Evaluator::getPositionHash(const Position& position) const
{
    return hasher_.getValue(position);
}

}
