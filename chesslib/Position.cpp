#include "Position.h"

#include "board_utils.h"
#include "require.h"
#include "squares.h"

namespace chesslib {

Position::Position(
    square_t blackKingSquare,
    square_t whiteKingSquare,
    player_t playerToMove,
    uint32_t halfmoveClock,
    uint32_t fullmoveNumber)
    : board_(blackKingSquare, whiteKingSquare)
    , moveCounters_(halfmoveClock, fullmoveNumber)
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
    piece_type_t capturedPieceType = NoPiece;

    // TODO
    if (move.isCapture())
    {
        capturedPieceType = move.getCapturedPieceType();
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
        if (move.isLongCastle()) {
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

    if (move.isPromotion()) {
        board_.promotePawn(destSquare, move.getPromoteToPieceType());
    }

    positionFlags_.onMovePlayed();

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

    if (capturedPieceType == Rook) {
        // TODO: Optimize me
        const square_t otherBaseRow = playerToMove == Black ? 0 : MAX_ROW;
        const player_t otherPlayer = getOtherPlayer();
        CastleOptions otherCastleOptions = getCastleOptions(otherPlayer);
        if (otherCastleOptions.isCanCastleLong() && destSquare == encodeSquare(otherBaseRow, COLUMN_A)) {
            otherCastleOptions.setCanCastleLong(false);
        }
        else if (otherCastleOptions.isCanCastleShort() && destSquare == encodeSquare(otherBaseRow, COLUMN_H)) {
            otherCastleOptions.setCanCastleShort(false);
        }
        positionFlags_.setCastleOptions(otherPlayer, otherCastleOptions);
    }

    positionFlags_.setEnPassantColumn(
        move.isPawnDoubleMove() ? OptionalColumn::fromColumn(getColumn(originSquare)) : OptionalColumn());

    positionFlags_.setPlayerToMove(getOtherPlayer());

    history_.emplace_back(move, oldPositionFlags, moveCounters_);

    uint32_t newHalfmoveClock = (move.isCapture() || pieceToMove == Pawn) ? 0 : moveCounters_.getHalfmoveClock() + 1;
    uint32_t newFullmoveNumber = moveCounters_.getFullmoveNumber() + (playerToMove == Black ? 1 : 0);
    moveCounters_ = PositionMoveCounters(newHalfmoveClock, newFullmoveNumber);
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
        if (move.isLongCastle()) {
            square_t rookOriginSquare = encodeSquare(originRow, COLUMN_A);
            square_t rookDestSquare = encodeSquare(originRow, COLUMN_D);
            board_.updatePieceSquare(rookDestSquare, rookOriginSquare);
        } else if (move.isShortCastle()) {
            square_t rookOriginSquare = encodeSquare(originRow, COLUMN_H);
            square_t rookDestSquare = encodeSquare(originRow, COLUMN_F);
            board_.updatePieceSquare(rookDestSquare, rookOriginSquare);
        }
    }

    if (move.isPromotion()) {
        board_.demoteToPawn(originSquare);
    }

    positionFlags_ = historyToUndo.getPositionFlags();
    moveCounters_ = historyToUndo.getPositionMoveCounters();

    history_.pop_back();
}

bool Position::isValid() const
{
    const player_t me = getOtherPlayer();
    const player_t attacker = getPlayerToMove();
    const square_t myKingSquare = board_.getKingSquare(me);

    if (!history_.empty()) {
        const auto parentIsInCheck = history_.back().getPositionFlags().getIsInCheck();
        if (parentIsInCheck.isEquals(false)) {
            const Move lastMove = history_.back().getMove();
            if (lastMove.getPieceType() != King) {
                const square_t originSquare = lastMove.getOriginSquare();
                const bool isMyKingBecameExposed = isSquareSlideAttackedThroughSpecificSquare(
                    originSquare, myKingSquare, board_, attacker);
                return !isMyKingBecameExposed;
            }
        }
    }

    const bool isMyKingCanBeCaptured = isSquareAttacked(myKingSquare, board_, attacker);
    return !isMyKingCanBeCaptured;
}

bool Position::isInCheck()
{
    const OptionalBoolean cachedResult = positionFlags_.getIsInCheck();
    if (cachedResult.hasValue()) {
        return cachedResult.getValue();
    }

    bool result;
    if (history_.empty()) {
        const square_t kingSquare = board_.getKingSquare(getPlayerToMove());
        result = isSquareAttacked(kingSquare, board_, getOtherPlayer());
    }
    else {
        result = isRecentMovePutsEnemyKingInCheck(history_.back().getMove());
    }

    positionFlags_.setIsInCheck(result);
    return result;
}

bool Position::isRecentMovePutsEnemyKingInCheck(Move recentMove) const
{
    const player_t otherPlayer = getOtherPlayer();
    const square_t kingSquare = board_.getKingSquare(getPlayerToMove());
    const square_t originSquare = recentMove.getOriginSquare();
    const square_t destSquare = recentMove.getDestSquare();
    if (recentMove.isCastle()) {
        const square_t rookDestColumn = (getColumn(originSquare) + getColumn(destSquare)) >> 1;
        const square_t rookDestSquare = encodeSquare(getRow(destSquare), rookDestColumn);
        return isSquareAttackedFromSpecificSquare(rookDestSquare, kingSquare, board_, otherPlayer);
    }
    else {
        return isSquareAttackedFromSpecificSquare(destSquare, kingSquare, board_, otherPlayer)
            || isSquareSlideAttackedThroughSpecificSquare(
                originSquare, kingSquare, board_, otherPlayer)
            || (recentMove.isEnPassantCapture()
                && isSquareSlideAttackedThroughSpecificSquare(
                    encodeSquare(getRow(originSquare), getColumn(destSquare)), kingSquare, board_, otherPlayer));
    }
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
