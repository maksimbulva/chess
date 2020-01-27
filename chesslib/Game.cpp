#include "Game.h"
#include "require.h"

#include <random>

namespace chesslib {

Game::Game(const Position& position)
    : initialPosition_(position)
    , currentPosition_(position)
    , memoryPool_(1)
{
    legalMoves_.reserve(256);
    updateLegalMoves();
}

Move Game::getRandomMove() const
{
    REQUIRE(!legalMoves_.empty());
    std::default_random_engine rnd;
    std::uniform_int_distribution<std::size_t> randomIndex{ 0, legalMoves_.size() - 1 };
    return legalMoves_[randomIndex(rnd)];
}

void Game::playMove(Move move)
{
    currentPosition_.playMove(move);
    currentPosition_.updateMoveCounters(move);
    updateLegalMoves();
}

void Game::updateLegalMoves()
{
    legalMoves_.clear();

    auto pseudoLegalMoves = currentPosition_.generatePseudoLegalMoves(
        Position::MoveGenerationFilter::AllMoves,
        memoryPool_);

    Position tmpPosition = currentPosition_;

    // TODO: optimize me
    for (const auto& scoredMove : *pseudoLegalMoves) {
        tmpPosition.playMove(scoredMove.getMove());
        if (tmpPosition.isValid()) {
            legalMoves_.push_back(scoredMove.getMove());
        }
        tmpPosition.undoMove();
    }
}

}
