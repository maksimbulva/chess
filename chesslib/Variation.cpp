#include "Variation.h"

namespace chesslib {

Variation::Variation(evaluation_t evaluation, const MovesCollection& moves)
    : evaluation_(evaluation)
{
    moves_.reserve(moves.size());
    for (const auto& scoredMove : moves) {
        moves_.push_back(scoredMove.getMove());
    }
}

}
