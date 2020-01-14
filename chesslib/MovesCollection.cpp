#include "MovesCollection.h"

#include "Evaluator.h"

#include <algorithm>

namespace chesslib {

void MovesCollection::append(const MovesCollection& other)
{
    std::copy(other.begin(), other.end(), end());
    bufferSize_ += other.bufferSize_;
}

void MovesCollection::scoreByMaterialGain()
{
    for (ScoredMove& scoredMove : *this) {
        scoredMove.score_ = Evaluator::getMaterialGain(scoredMove.getMove());
    }
    sortMoves();
}

void MovesCollection::scoreByTableValueDelta(player_t playerToMove)
{
    for (ScoredMove& scoredMove : *this) {
        scoredMove.score_ = Evaluator::getTableValueDelta(scoredMove.getMove(), playerToMove);
    }
    sortMoves();
}

void MovesCollection::sortMoves()
{
    std::sort(begin(), end(), [] (const ScoredMove& lhs, const ScoredMove& rhs)
        {
            return lhs.getScore() > rhs.getScore();
        });
}

}
