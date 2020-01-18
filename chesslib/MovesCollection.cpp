#include "MovesCollection.h"

#include "Evaluator.h"

#include <algorithm>
#include <limits>

namespace chesslib {

void MovesCollection::append(const MovesCollection& other)
{
    std::copy(other.begin(), other.end(), end());
    bufferSize_ += other.bufferSize_;
}

void MovesCollection::scoreMoves(
    const Evaluator& evaluator,
    player_t playerToMove,
    Move moveToPrioritize)
{
    for (ScoredMove& scoredMove : *this) {
        scoredMove.updateScore(evaluator, playerToMove);
        if (scoredMove.getMove() == moveToPrioritize) {
            scoredMove.setScore(std::numeric_limits<evaluation_t>::max());
        }
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
