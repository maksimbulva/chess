#include "Player.h"

namespace chesslib {

namespace {

constexpr uint64_t DefaultMaxEvaluations = 5000000;

}

Player::Player()
    : maxEvaluations_(DefaultMaxEvaluations)
{
}

}
