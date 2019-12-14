#include "Position.h"

#include "board_utils.h"
#include "require.h"

namespace chesslib {

Position::Position(
    square_t blackKingSquare,
    square_t whiteKingSquare,
    player_t playerToMove)
    : board_(blackKingSquare, whiteKingSquare)
{
    positionFlags_.setPlayerToMove(playerToMove);
}

void Position::addPiece(const PieceOnBoard& piece)
{
    board_.addPiece(piece);
}

void Position::playMove(const Move& move)
{
    const auto oldPositionFlags = positionFlags_;

    // TODO
    if (move.isCapture())
    {
        if (move.isEnPassantCapture())
        {
            // TODO
            FAIL();
        }
        // TODO
        FAIL();
    }
    else {
        board_.updatePieceSquare(move.getOriginSquare(), move.getDestSquare());
        if (move.isPromotion()) {
            // TODO
            FAIL();
        } else if (move.isShortCastle()) {
            // TODO
            FAIL();
        } else if (move.isLongCastle()) {
            // TODO
            FAIL();
        }
    }

    positionFlags_.setPlayerToMove(getOtherPlayer());

    // TODO: Update castling options
    // TODO: Update en passant capture possibility

    history_.emplace_back(move, oldPositionFlags);
}

void Position::undoMove()
{
    if (!isCanUndoMove()) {
        FAIL();
    }

    const PositionHistory& historyToUndo = history_.back();
    const Move move = historyToUndo.getMove();

    // TODO
    if (move.isCapture())
    {
        if (move.isEnPassantCapture())
        {
            // TODO
            FAIL();
        }
        // TODO
        FAIL();
    }
    else {
        board_.updatePieceSquare(move.getDestSquare(), move.getOriginSquare());
        if (move.isPromotion()) {
            // TODO
            FAIL();
        } else if (move.isShortCastle()) {
            // TODO
            FAIL();
        } else if (move.isLongCastle()) {
            // TODO
            FAIL();
        }
    }

    positionFlags_ = historyToUndo.getPositionFlags();

    history_.pop_back();
}

bool Position::isKingCanBeCaptured() const
{
    // TODO: Add various optimizations
    // TODO: Check if this is perf-critical (most likely yes)
    const player_t attacker = getPlayerToMove();
    const player_t otherPlayer = getOtherPlayer();
    const square_t kingSquare = board_.getKingSquare(otherPlayer);
    return isSquareAttacked(kingSquare, board_, attacker);
}

}
