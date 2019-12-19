#include "Position.h"

#include "board_utils.h"
#include "require.h"
#include "squares.h"

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
    const player_t playerToMove = getPlayerToMove();
    
    const square_t originSquare = move.getOriginSquare();
    const square_t originRow = getRow(originSquare);
    const square_t destSquare = move.getDestSquare();

    // TODO
    if (move.isCapture())
    {
        if (move.isEnPassantCapture())
        {
            const auto capturedPieceSquare = encodeSquare(
                originRow, getColumn(destSquare));
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
        else if (move.isLongCastle()) {
            square_t rookOriginSquare = encodeSquare(originRow, COLUMN_A);
            square_t rookDestSquare = encodeSquare(originRow, COLUMN_D);
            board_.updatePieceSquare(rookOriginSquare, rookDestSquare);
        }
        else if (move.isShortCastle()) {
            square_t rookOriginSquare = encodeSquare(originRow, COLUMN_H);
            square_t rookDestSquare = encodeSquare(originRow, COLUMN_F);
            board_.updatePieceSquare(rookOriginSquare, rookDestSquare);
        }
    }

    CastleOptions castleOptions = getCastleOptions(playerToMove);
    const piece_type_t pieceToMove = move.getPieceType();
    if (pieceToMove == King) {
        castleOptions = CastleOptions();
    }
    else if (pieceToMove == Rook) {
        if (castleOptions.isCanCastleLong() && getColumn(originSquare) == COLUMN_A) {
            castleOptions.setCanCastleLong(false);
        }
        else if (castleOptions.isCanCastleShort() && getColumn(originSquare) == COLUMN_H) {
            castleOptions.setCanCastleShort(false);
        }
    }
    positionFlags_.setCastleOptions(playerToMove, castleOptions);

    positionFlags_.setEnPassantColumn(
        move.isPawnDoubleMove() ? OptionalColumn::fromColumn(getColumn(originSquare)) : OptionalColumn());

    positionFlags_.setPlayerToMove(getOtherPlayer());

    history_.emplace_back(move, oldPositionFlags);
}

void Position::undoMove()
{
    if (!isCanUndoMove()) {
        FAIL();
    }

    const PositionHistory& historyToUndo = history_.back();
    const player_t playerToMove = getPlayerToMove();

    const Move move = historyToUndo.getMove();
    const square_t originSquare = move.getOriginSquare();
    const square_t originRow = getRow(originSquare);
    const square_t destSquare = move.getDestSquare();

    // TODO
    if (move.isCapture())
    {
        // TODO
        board_.updatePieceSquare(destSquare, originSquare);

        const square_t capturedPieceSquare = move.isEnPassantCapture()
            ? encodeSquare(originRow, getColumn(destSquare))
            : destSquare;
        board_.addPiece({ playerToMove, move.getCapturedPieceType(), capturedPieceSquare });
    }
    else {
        board_.updatePieceSquare(destSquare, originSquare);
        if (move.isPromotion()) {
            // TODO
            FAIL();
        } else if (move.isLongCastle()) {
            square_t rookOriginSquare = encodeSquare(originRow, COLUMN_A);
            square_t rookDestSquare = encodeSquare(originRow, COLUMN_D);
            board_.updatePieceSquare(rookDestSquare, rookOriginSquare);
        } else if (move.isShortCastle()) {
            square_t rookOriginSquare = encodeSquare(originRow, COLUMN_H);
            square_t rookDestSquare = encodeSquare(originRow, COLUMN_F);
            board_.updatePieceSquare(rookDestSquare, rookOriginSquare);
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

bool Position::isInCheck() const
{
    // TODO: Optimize me, store the result for re-use
    const square_t kingSquare = board_.getKingSquare(getPlayerToMove());
    return isSquareAttacked(kingSquare, board_, getOtherPlayer());
}

namespace {

CastleOptions optimize(CastleOptions castleOptions, const Board& board, player_t player)
{
    CastleOptions result;
    const square_t row = player == Black ? MAX_ROW : 0;
    if (!board.isPlayerPiece(encodeSquare(row, COLUMN_E), player, King)) {
        return result;
    }

    result.setCanCastleLong(
        castleOptions.isCanCastleLong()
        && board.isPlayerPiece(encodeSquare(row, COLUMN_A), player, Rook));

    result.setCanCastleShort(
        castleOptions.isCanCastleShort()
        && board.isPlayerPiece(encodeSquare(row, COLUMN_H), player, Rook));

    return result;
}

}  // namespace

void Position::optimizeCastleOptions()
{
    for (player_t player = 0; player < PLAYER_COUNT; ++player) {
        setCastleOptions(player, optimize(getCastleOptions(player), board_, player));
    }
}

}
