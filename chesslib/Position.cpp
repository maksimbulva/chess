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
    
    const square_t originSquare = move.getOriginSquare();
    const square_t destSquare = move.getDestSquare();

    // TODO
    if (move.isCapture())
    {
        if (move.isEnPassantCapture())
        {
            const auto capturedPieceSquare = encodeSquare(
                getRow(originSquare), getColumn(destSquare));
            board_.erasePieceAt(capturedPieceSquare);
        }
        else {
            // TODO
            // TODO: optimization - there is no need to clear the dest square first
            board_.erasePieceAt(destSquare);
        }
        board_.updatePieceSquare(originSquare, destSquare);
    }
    else {
        board_.updatePieceSquare(originSquare, destSquare);
        if (move.isPromotion()) {
            // TODO
            FAIL();
        }
        else if (move.isShortCastle()) {
            // TODO
            FAIL();
        }
        else if (move.isLongCastle()) {
            // TODO
            FAIL();
        }
    }

    positionFlags_.setPlayerToMove(getOtherPlayer());
    positionFlags_.setEnPassantColumn(
        move.isPawnDoubleMove() ? OptionalColumn(getColumn(originSquare)) : OptionalColumn());

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
    const square_t originSquare = move.getOriginSquare();
    const square_t destSquare = move.getDestSquare();

    // TODO
    if (move.isCapture())
    {
        // TODO
        board_.updatePieceSquare(destSquare, originSquare);

        const square_t capturedPieceSquare = move.isEnPassantCapture()
            ? encodeSquare(getRow(originSquare), getColumn(destSquare))
            : destSquare;
        board_.addPiece({ getPlayerToMove(), move.getCapturedPieceType(), capturedPieceSquare });
    }
    else {
        board_.updatePieceSquare(destSquare, originSquare);
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
