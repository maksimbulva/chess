#include "MovesCollection.h"

#include "Evaluator.h"

#include <algorithm>

namespace chesslib {

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
