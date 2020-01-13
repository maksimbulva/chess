#include "testing_utils.h"

#include "fen.h"
#include "Position.h"

namespace chesslib {

namespace {

uint64_t countLegalMovesRecursively(Position& position, int depthPly)
{
    if (depthPly == 0) {
        return 1;
    }

    uint64_t result = 0;

    MovesCollection moves;
    position.fillWithPseudoLegalMoves(moves, Position::MoveGenerationFilter::AllMoves);

    for (const auto& scoredMove : moves) {
        position.playMove(scoredMove.getMove());
        if (position.isValid()) {
            if (depthPly == 1) {
                ++result;
            }
            else {
                result += countLegalMovesRecursively(position, depthPly - 1);
            }
        }
        position.undoMove();
    }

    return result;
}

}

uint64_t countLegalMoves(std::string fenString, int depthPly)
{
    Position initialPosition = decodeFen(fenString);
    return countLegalMovesRecursively(initialPosition, depthPly);
}

}
