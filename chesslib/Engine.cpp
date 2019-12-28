#include "Engine.h"

#include "evaluate.h"
#include "fen.h"
#include "minmax.h"
#include "SearchTree.h"

#include <algorithm>

namespace chesslib {

namespace {

const char* const initialPositonFen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

void generateChildren(SearchNode& parent, Position& position)
{
    MovesCollection pseudoLegalMoves;
    position.fillWithPseudoLegalMoves(pseudoLegalMoves);
    for (Move move : pseudoLegalMoves) {
        position.playMove(move);
        if (position.isValid()) {
            auto addedNode = parent.addChild(move);
            // TODO: temporary logic - just evaluate all generated nodes
            double evaluation = evaluate(position);
            addedNode->setEvaluation(evaluation);
        }
        position.undoMove();
    }
}

}   // namespace

Engine::Engine()
    : game_(decodeFen(initialPositonFen))
{
}

std::string Engine::getName() const
{
    return "Chesslib v0.01";
}

void Engine::resetGame()
{
    game_ = Game{ decodeFen(initialPositonFen) };
}

bool Engine::playMove(square_t originSquare, square_t destSquare)
{
    const auto& legalMoves = game_.getLegalMoves();
    const auto moveIter = std::find_if(legalMoves.begin(), legalMoves.end(),
        [originSquare, destSquare] (const Move& move)
        {
            return move.getOriginSquare() == originSquare
                && move.getDestSquare() == destSquare
                && !move.isPromotion();
        });

    if (moveIter != legalMoves.end()) {
        game_.playMove(*moveIter);
        return true;
    }
    else {
        return false;
    }
}

Move Engine::findBestMove()
{
    auto currentPosition = game_.getCurrentPosition();
    SearchTree searchTree{ currentPosition };
    generateChildren(searchTree.getRoot(), currentPosition);
    const SearchNode* bestNode = findBestChildNode(searchTree.getRoot(), currentPosition.getPlayerToMove());
    if (bestNode == nullptr) {
        return Move::NullMove();
    }
    return bestNode->getMove();
}

}
