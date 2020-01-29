#include "Player.h"

namespace chesslib {

namespace {

constexpr uint64_t DefaultMaxEvaluations = 5000000;

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

}
