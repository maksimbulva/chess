#include "Position.h"

#include "bitboard.h"
#include "board_utils.h"
#include "require.h"

namespace chesslib {

namespace {

struct KnightDirection {
    constexpr KnightDirection(fastint deltaRow, fastint deltaColumn)
        : delta(getDelta(deltaRow, deltaColumn))
        , origins(generateValidOriginsForDelta(deltaRow, deltaColumn))
    {
    }

    const square_t delta;
    const bitboard_t origins;
};

constexpr std::array<KnightDirection, 8> KNIGHT_DIRECTIONS = {
    KnightDirection(-2, -1),
    KnightDirection(-2, 1),
    KnightDirection(-1, -2),
    KnightDirection(-1, 2),
    KnightDirection(1, -2),
    KnightDirection(1, 2),
    KnightDirection(2, -1),
    KnightDirection(2, 1)
};

}  // namespace

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

void Position::fillWithLegalMoves(MovesCollection& moves) const
{
    // TODO
    fillWithPseudoLegalMoves(moves);
}

void Position::fillWithPseudoLegalMoves(MovesCollection& moves) const
{
    _ASSERT(moves.empty());

    const player_t myPlayer = getPlayerToMove();
    auto myPiecesIt = board_.getPieceIterator(myPlayer);

    while (true) {

        switch (myPiecesIt.getPieceType())
        {
        case Pawn:
            fillWithPawnMoves(myPiecesIt.getSquare(), moves);
            break;
        case Knight:
            fillWithKnightMoves(myPiecesIt.getSquare(), moves);
            break;
        case Bishop:
            break;
        case Rook:
            break;
        case Queen:
            break;
        case King:
            break;
        default:
            FAIL();
            return;
        }

        if (myPiecesIt.hasNext()) {
            ++myPiecesIt;
        }
        else {
            break;
        }
    }
}

void Position::fillWithPawnMoves(square_t pawnSquare, MovesCollection& moves) const
{
    square_t forward;
    square_t initialRow;

    if (getPlayerToMove() == Black) {
        forward = DIRECTION_DOWN;
        initialRow = BLACK_PAWN_ROW;
    }
    else {
        forward = DIRECTION_UP;
        initialRow = WHITE_PAWN_ROW;
    }

    const auto singleMoveForwardSquare = pawnSquare + forward;
    if (board_.isEmpty(singleMoveForwardSquare)) {
        moves.push_back(Move(pawnSquare, singleMoveForwardSquare));
        const auto doubleMoveForwardSquare = pawnSquare + 2 * forward;
        if (board_.isEmpty(doubleMoveForwardSquare)) {
            moves.push_back(Move(pawnSquare, doubleMoveForwardSquare));
        }
    }

    // TODO
}

void Position::fillWithKnightMoves(square_t knightSquare, MovesCollection& moves) const
{
    const bitboard_t originBit = (static_cast<bitboard_t>(1)) << knightSquare;

    for (const auto& knightDirection : KNIGHT_DIRECTIONS) {
        if (knightDirection.origins & originBit) {
            const square_t destSquare = knightSquare + knightDirection.delta;
            if (board_.isEmpty(destSquare)) {
                moves.push_back(Move(knightSquare, destSquare));
            }
            else {
                // TODO
            }
        }
    }
}

}
