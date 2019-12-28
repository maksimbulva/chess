#include "board_utils.h"

#include "Board.h"
#include "MoveDelta.h"

#include <algorithm>

namespace chesslib {

namespace {

constexpr fastint FLAG_DIRECTION_LEFT = 1 << 0;
constexpr fastint FLAG_DIRECTION_RIGHT = 1 << 1;
constexpr fastint FLAG_DIRECTION_UP = 1 << 2;
constexpr fastint FLAG_DIRECTION_DOWN = 1 << 3;

constexpr fastint FLAG_DIRECTION_UP_LEFT = 1 << 4;
constexpr fastint FLAG_DIRECTION_UP_RIGHT = 1 << 5;
constexpr fastint FLAG_DIRECTION_DOWN_LEFT = 1 << 6;
constexpr fastint FLAG_DIRECTION_DOWN_RIGHT = 1 << 7;

bool isSlideAttack(
    square_t target,
    square_t reversedDirection,
    const Board& board,
    player_t attacker,
    piece_type_t pieceType)
{
    square_t currentSquare = target;
    while (true) {
        currentSquare += reversedDirection;
        if (board.isNotEmpty(currentSquare)) {
            return board.isPlayerSlider(currentSquare, attacker, pieceType);
        }
    }
    return false;
}

}  // namespace

bool isSquareAttacked(square_t target, const Board& board, player_t attacker)
{
    const square_t targetRow = getRow(target);
    const square_t targetColumn = getColumn(target);

    const square_t attackerPawnsRow = attacker == Black ? targetRow + 1 : targetRow - 1;
    if (attackerPawnsRow >= 0 && attackerPawnsRow < ROW_COUNT) {
        if (targetColumn > 0) {
            square_t square = encodeSquare(attackerPawnsRow, targetColumn - 1);
            if (board.isPlayerPiece(square, attacker, Pawn)) {
                return true;
            }
        }
        if (targetColumn < MAX_COLUMN) {
            square_t square = encodeSquare(attackerPawnsRow, targetColumn + 1);
            if (board.isPlayerPiece(square, attacker, Pawn)) {
                return true;
            }
        }
    }

    fastint checkDirFlags = 0;

    auto piecesIt = board.getPieceIterator(attacker);

    while (true) {
        const square_t attackOrigin = piecesIt.getSquare();
        // TODO: get rid of these low-level logic
        const square_t attackOriginRow = getRow(attackOrigin);
        const square_t attackOriginColumn = getColumn(attackOrigin);

        const MoveDelta moveDelta(attackOrigin, target);

        const piece_type_t pieceType = piecesIt.getPieceType();
        switch (pieceType) {
        case Pawn:
            // Already checked
            break;
        case Knight:
        {
            if (moveDelta.isKnightDelta()) {
                return true;
            }
        }
            break;
        case King:
        {
            if (moveDelta.isKingDelta()) {
                return true;
            }
        }
        default:
        {
            const auto deltaRow = attackOriginRow - targetRow;
            const auto deltaColumn = attackOriginColumn - targetColumn;

            if (pieceType == Bishop || pieceType == Queen)
            {
                if (deltaRow == deltaColumn) {
                    checkDirFlags |= (deltaRow > 0) ? FLAG_DIRECTION_UP_RIGHT : FLAG_DIRECTION_DOWN_LEFT;
                }
                else if (deltaRow == -deltaColumn) {
                    checkDirFlags |= (deltaRow > 0) ? FLAG_DIRECTION_UP_LEFT : FLAG_DIRECTION_DOWN_RIGHT;
                }
            }
            if (pieceType == Rook || pieceType == Queen)
            {
                if (deltaRow == 0) {
                    checkDirFlags |= (deltaColumn < 0) ? FLAG_DIRECTION_LEFT : FLAG_DIRECTION_RIGHT;
                }
                else if (deltaColumn == 0) {
                    checkDirFlags |= (deltaRow > 0) ? FLAG_DIRECTION_UP : FLAG_DIRECTION_DOWN;
                }
            }
            break;
        }
        }

        if (piecesIt.hasNext()) {
            ++piecesIt;
        }
        else {
            break;
        }
    }

    if (checkDirFlags == 0) {
        return false;
    }

    return (((checkDirFlags & FLAG_DIRECTION_LEFT)
        && isSlideAttack(target, DIRECTION_LEFT, board, attacker, Rook))
        || ((checkDirFlags & FLAG_DIRECTION_RIGHT)
            && isSlideAttack(target, DIRECTION_RIGHT, board, attacker, Rook))
        || ((checkDirFlags & FLAG_DIRECTION_UP)
            && isSlideAttack(target, DIRECTION_UP, board, attacker, Rook))
        || ((checkDirFlags & FLAG_DIRECTION_DOWN)
            && isSlideAttack(target, DIRECTION_DOWN, board, attacker, Rook))
        || ((checkDirFlags & FLAG_DIRECTION_UP_LEFT)
            && isSlideAttack(target, DIRECTION_UP_LEFT, board, attacker, Bishop))
        || ((checkDirFlags & FLAG_DIRECTION_UP_RIGHT)
            && isSlideAttack(target, DIRECTION_UP_RIGHT, board, attacker, Bishop))
        || ((checkDirFlags & FLAG_DIRECTION_DOWN_LEFT)
            && isSlideAttack(target, DIRECTION_DOWN_LEFT, board, attacker, Bishop))
        || ((checkDirFlags & FLAG_DIRECTION_DOWN_RIGHT)
            && isSlideAttack(target, DIRECTION_DOWN_RIGHT, board, attacker, Bishop)));
}

bool isSquareAttackedFromSpecificSquare(
    square_t origin,
    square_t target,
    const Board& board,
    player_t attacker)
{
    const piece_type_t pieceType = board.getPieceTypeAt(origin);
    const MoveDelta moveDelta(origin, target);

    switch (pieceType) {
    case Pawn:
        return moveDelta.isPawnDelta(attacker);
    case Knight:
        return moveDelta.isKnightDelta();
    case King:
        return moveDelta.isKingDelta();
    }

    square_t direction = DIRECTION_NONE;
    if ((pieceType == Bishop) || (pieceType == Queen)) {
        direction = moveDelta.toBishiopDirection();
    }
    if (direction == DIRECTION_NONE && (pieceType == Rook || pieceType == Queen)) {
        direction = moveDelta.toRookDirection();
    }

    if (direction == DIRECTION_NONE) {
        return false;
    }

    // Check if there is any blocker between attacker and target
    for (square_t currentSquare = origin + direction; currentSquare != target; currentSquare += direction) {
        if (!board.isEmpty(currentSquare)) {
            return false;
        }
    }

    return true;
}

bool isSquareSlideAttackedThroughSpecificSquare(
    square_t intermediateSquare,
    square_t target,
    const Board& board,
    player_t attacker)
{
    MoveDelta reversedMoveDelta(target, intermediateSquare);
    RayIterator rayIter = createRayIterator(target, reversedMoveDelta);

    const piece_type_t rayPieceType = rayIter.getPieceType();
    
    while (rayIter.hasNext()) {
        ++rayIter;
        auto currentSquare = rayIter.currentSquare();
        if (board.isNotEmpty(currentSquare)) {
            return board.isPlayerSlider(currentSquare, attacker, rayPieceType);
        }
    }

    return false;
}

}
