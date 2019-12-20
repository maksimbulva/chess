#include "Engine.h"

#include "fen.h"

#include <algorithm>

namespace chesslib {

namespace {

const char* const initialPositonFen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

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

}
