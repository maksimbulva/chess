#include "Position.h"

#include "board_utils.h"

namespace chesslib {

Position::Position(
    square_t blackKingSquare,
    square_t whiteKingSquare)
    : board_(blackKingSquare, whiteKingSquare)
{

}

void Position::addPiece(const PieceOnBoard& piece)
{
    board_.addPiece(piece);
}

void Position::playMove(const Move& move)
{
    // TODO
    (void)move;
}

void Position::undoMove()
{
    // TODO
}

bool Position::isKingCanBeCaptured() const
{
    // TODO: Add various optimizations
    // TODO: Check if this is perf-critical (most likely yes)
    const player_t attacker = getPlayerToMove();
    const player_t otherPlayer = getOtherPlayer();
    const square_t kingSquare = board_.getKingSquare(otherPlayer);
    return isSquareAttacked(kingSquare, board_, getPlayerToMove());
}

}
