#include "Player.h"

#include "require.h"

namespace chesslib {

namespace {

constexpr uint64_t DefaultMaxEvaluations = 5000000;

bool isPowerOfTwo(evaluation_t value)
{
    return value > 0 && (value & (value - 1)) == 0;
}

}

Player::Player()
    : maxEvaluations_(DefaultMaxEvaluations)
    , evaluationRandomness_(0)
{
}

void Player::setMaxEvaluations(uint64_t maxEvaluations)
{
    maxEvaluations_ = maxEvaluations;
}

void Player::setEvaluationRandomness(evaluation_t evaluationRandomness)
{
    REQUIRE(evaluationRandomness == 0 || isPowerOfTwo(evaluationRandomness));
    evaluationRandomness_ = evaluationRandomness;
}

void Player::setMaterialValue(piece_type_t pieceType, evaluation_t materialValue)
{
    REQUIRE(pieceType >= Pawn && pieceType <= King);
    evaluator_.setMaterialValue(pieceType, materialValue); 
}

}
