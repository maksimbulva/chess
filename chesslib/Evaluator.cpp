#include "evaluate.h"

#include "Position.h"

#include <array>

namespace chesslib {

namespace {

constexpr double CheckmateValue = 1000.0;
constexpr double StalemateValue = 0.0;

static std::array<fastint, King + 1> MATERIAL_VALUE = {
    /* NoPiece */ 0,
    /* Pawn*/     1,
    /* Knight */  3,
    /* Bishop */  3,
    /* Rook */    5,
    /* Queen */   9,
    /* King */    0
};

fastint evaluateMaterial(const Position& position, player_t player)
{
    fastint result = 0;
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

// For evaluation based on https://www.chessprogramming.org/Simplified_Evaluation_Function
std::array<double, SQUARE_COUNT> TABLE_PAWN_VALUES = {
    0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
    0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5,
    0.1, 0.1, 0.2, 0.3, 0.3, 0.2, 0.1, 0.1,
    0.05, 0.05, 0.1, 0.25, 0.25, 0.1, 0.05, 0.05,
    0.0, 0.0, 0.0, 0.2, 0.2, 0.0, 0.0, 0.0,
    0.05, -0.05, -0.1, 0.0, 0.0, -0.1, -0.05, 0.05,
    0.5, 0.1, 0.1, -0.2, -0.2, 0.1, 0.1, 0.5,
    0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0
};

std::array<double, SQUARE_COUNT> TABLE_KNIGHT_VALUES = {
    -0.5, -0.4, -0.3, -0.3, -0.3, -0.3, -0.4, -0.5,
    -0.4, -0.2, 0.0, 0.0, 0.0, 0.0, -0.2, -0.4,
    -0.3, 0.0, 0.1, 0.15, 0.15, 0.1, 0.0, -0.3,
    -0.3, 0.05, 0.15, 0.2, 0.2, 0.15, 0.05, -0.3,
    -0.3, 0.0, 0.15, 0.2, 0.2, 0.15, 0.0, -0.3,
    -0.3, 0.05, 0.1, 0.15, 0.15, 0.10, 0.05, -0.3,
    -0.4, -0.2, 0.0, 0.05, 0.05, 0.0, -0.2, -0.4,
    -0.5, -0.4, -0.3, -0.3, -0.3, -0.3, -0.4, -0.5
};

std::array<double, SQUARE_COUNT> TABLE_BISHOP_VALUES = {
    -0.2, -0.1, -0.1, -0.1, -0.1, -0.1, -0.1, -0.2,
    -0.1, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.1,
    -0.1, 0.0, 0.05, 0.1, 0.1, 0.05, 0.0, -0.1,
    -0.1, 0.05, 0.05, 0.1, 0.1, 0.05, 0.05, -0.1,
    -0.1, 0.0, 0.1, 0.1, 0.1, 0.1, 0.0, -0.1,
    -0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, -0.1,
    -0.1, 0.05, 0.0, 0.0, 0.0, 0.0, 0.05, -0.1,
    -0.2, -0.1, -0.1, -0.1, -0.1, -0.1, -0.1, -0.2
};

std::array<double, SQUARE_COUNT> TABLE_ROOK_VALUES = {
    0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
    0.05, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.05,
    -0.05, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.05,
    -0.05, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.05,
    -0.05, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.05,
    -0.05, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.05,
    -0.05, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.05,
    0.0, 0.0, 0.0, 0.05, 0.05, 0.0, 0.0, 0.0
};

std::array<double, SQUARE_COUNT> TABLE_QUEEN_VALUES = {
    -0.2, -0.1, -0.1, -0.05, -0.05, -0.1, -0.1, -0.2,
    -0.1, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.1,
    -0.1, 0.0, 0.05, 0.05, 0.05, 0.05, 0.0, -0.1,
    -0.05, 0.0, 0.05, 0.05, 0.05, 0.05, 0.0, -0.05,
    0.0, 0.0, 0.05, 0.05, 0.05, 0.05, 0.0, -0.05,
    -0.1, 0.05, 0.05, 0.05, 0.05, 0.05, 0.0, -0.1,
    -0.1, 0.0, 0.05, 0.0, 0.0, 0.0, 0.0, -0.1,
    -0.2, -0.1, -0.1, -0.05, -0.05, -0.1, -0.1, -0.2,
};

std::array<double, SQUARE_COUNT> TABLE_KING_VALUES = {
    -0.3, -0.4, -0.4, -0.5, -0.5, -0.4, -0.4, -0.3,
    -0.3, -0.4, -0.4, -0.5, -0.5, -0.4, -0.4, -0.3,
    -0.3, -0.4, -0.4, -0.5, -0.5, -0.4, -0.4, -0.3,
    -0.3, -0.4, -0.4, -0.5, -0.5, -0.4, -0.4, -0.3,
    -0.2, -0.3, -0.3, -0.4, -0.4, -0.3, -0.3, -0.2,
    -0.1, -0.2, -0.2, -0.2, -0.2, -0.2, -0.2, -0.1,
    0.2, 0.2, 0.0, 0.0, 0.0, 0.0, 0.2, 0.2,
    0.2, 0.3, 0.1, 0.0, 0.0, 0.1, 0.3, 0.2
};

// TODO: For endgame or midgame we can consider another king table value

std::array<std::array<double, SQUARE_COUNT>*, King + 1> TABLE_VALUES = {
    /* NoPiece */ nullptr,
    &TABLE_PAWN_VALUES,
    &TABLE_KNIGHT_VALUES,
    &TABLE_BISHOP_VALUES,
    &TABLE_ROOK_VALUES,
    &TABLE_QUEEN_VALUES,
    &TABLE_KING_VALUES
};

double evaluateTableValues(const Position& position, player_t player)
{
    double result = 0.0;
    auto piecesIt = position.getBoard().getPieceIterator(player);
    while (true) {
        square_t square = piecesIt.getSquare();
        if (square == Black) {
            square = encodeSquare(MAX_ROW - getRow(square), getColumn(square));
        }
        auto* table = TABLE_VALUES[piecesIt.getPieceType()];
        result += (*table)[square];
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

// TODO: this is temporary method. Replace with some ML evaluations
double evaluate(const Position& position)
{
    auto materialValueDiff = evaluateMaterial(position, White) - evaluateMaterial(position, Black);
    return static_cast<double>(materialValueDiff)
        + evaluateTableValues(position, White)
        - evaluateTableValues(position, Black);
}

double evaluateNoLegalMovesPosition(Position& position)
{
    return position.isInCheck() ? CheckmateValue : StalemateValue;
}

}
