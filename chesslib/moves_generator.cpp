#include "MoveBuilder.h"
#include "Position.h"

#include "bitboard.h"
#include "require.h"

namespace chesslib {

namespace {

struct MoveDelta {
    constexpr MoveDelta(fastint deltaRow, fastint deltaColumn)
        : delta(getDelta(deltaRow, deltaColumn))
        , origins(generateValidOriginsForDelta(deltaRow, deltaColumn))
    {
    }

    const square_t delta;
    const bitboard_t origins;
};

constexpr std::array<MoveDelta, 8> KNIGHT_MOVE_DELTAS = {
    MoveDelta(-2, -1),
    MoveDelta(-2, 1),
    MoveDelta(-1, -2),
    MoveDelta(-1, 2),
    MoveDelta(1, -2),
    MoveDelta(1, 2),
    MoveDelta(2, -1),
    MoveDelta(2, 1)
};

constexpr std::array<MoveDelta, 8> KING_MOVE_DELTAS = {
    MoveDelta(-1, -1),
    MoveDelta(-1, 0),
    MoveDelta(-1, 1),
    MoveDelta(0, -1),
    MoveDelta(0, 1),
    MoveDelta(1, -1),
    MoveDelta(1, 0),
    MoveDelta(1, 1)
};

void fillWithDeltaMoves(piece_type_t pieceType, square_t origin, const Position& position, MovesCollection& moves)
{
    const auto& deltas = pieceType == Knight ? KNIGHT_MOVE_DELTAS : KING_MOVE_DELTAS;
    const player_t myPlayer = position.getPlayerToMove();
    const Board& board = position.getBoard();
    const bitboard_t originBit = setSquare(0, origin);
    MoveBuilder moveBuilder{ pieceType, origin };

    for (const auto& delta : deltas) {
        if (delta.origins & originBit) {
            const square_t destSquare = origin + delta.delta;
            MoveBuilder mb = moveBuilder.setDestSquare(destSquare);
            if (board.isEmpty(destSquare)) {
                moves.push_back(mb.build());
            }
            else if (board.getPlayer(destSquare) != myPlayer) {
                moves.push_back(mb.setCapture(board).build());
            }
        }
    }

}

}  // namespace

void Position::fillWithLegalMoves(MovesCollection& moves) const
{
    // TODO
    fillWithPseudoLegalMoves(moves);
}

void Position::fillWithPseudoLegalMoves(MovesCollection& moves) const
{
    assert(moves.empty());

    const player_t myPlayer = getPlayerToMove();
    auto myPiecesIt = board_.getPieceIterator(myPlayer);

    while (true) {

        const square_t origin = myPiecesIt.getSquare();
        const piece_type_t pieceType = myPiecesIt.getPieceType();
        switch (pieceType)
        {
        case Pawn:
            fillWithPawnMoves(origin, moves);
            break;
        case Knight:
            fillWithKnightMoves(origin, moves);
            break;
        case King:
            fillWithKingMoves(origin, moves);
            break;
        default:
            if (pieceType == Rook || pieceType == Queen) {
                fillWithSlideMoves(pieceType, createRayIterator<DIRECTION_LEFT>(origin), moves);
                fillWithSlideMoves(pieceType, createRayIterator<DIRECTION_RIGHT>(origin), moves);
                fillWithSlideMoves(pieceType, createRayIterator<DIRECTION_DOWN>(origin), moves);
                fillWithSlideMoves(pieceType, createRayIterator<DIRECTION_UP>(origin), moves);
            }
            if (pieceType == Bishop || pieceType == Queen) {
                fillWithSlideMoves(pieceType, createRayIterator<DIRECTION_DOWN_LEFT>(origin), moves);
                fillWithSlideMoves(pieceType, createRayIterator<DIRECTION_DOWN_RIGHT>(origin), moves);
                fillWithSlideMoves(pieceType, createRayIterator<DIRECTION_UP_LEFT>(origin), moves);
                fillWithSlideMoves(pieceType, createRayIterator<DIRECTION_UP_RIGHT>(origin), moves);
            }
        }

        if (myPiecesIt.hasNext()) {
            ++myPiecesIt;
        }
        else {
            break;
        }
    }
}

namespace {

constexpr std::array<piece_type_t, 4> PROMOTION_PIECES = { Knight, Bishop, Rook, Queen };

void generatePromotions(MoveBuilder moveBuilder, MovesCollection& moves)
{
    for (auto promotionPiece : PROMOTION_PIECES) {
        moves.push_back(moveBuilder.setPromoteToPieceType(promotionPiece).build());
    }
}

}  // namespace

void Position::fillWithPawnMoves(square_t pawnSquare, MovesCollection& moves) const
{
    const auto pawnRow = getRow(pawnSquare);
    const auto pawnColumn = getColumn(pawnSquare);

    square_t forward;
    square_t initialRow;
    square_t prePromotionRow;
    square_t enPassantCaptureRow;

    if (getPlayerToMove() == Black) {
        forward = DIRECTION_DOWN;
        initialRow = BLACK_PAWN_ROW;
        prePromotionRow = 1;
        enPassantCaptureRow = 3;
    }
    else {
        forward = DIRECTION_UP;
        initialRow = WHITE_PAWN_ROW;
        prePromotionRow = MAX_ROW - 1;
        enPassantCaptureRow = 4;
    }

    const player_t myPlayer = getPlayerToMove();
    MoveBuilder moveBuilder{ Pawn, pawnSquare };

    const auto singleMoveForwardSquare = pawnSquare + forward;
    if (board_.isEmpty(singleMoveForwardSquare)) {
        if (pawnRow == prePromotionRow) {
            generatePromotions(moveBuilder.setDestSquare(singleMoveForwardSquare), moves);
        }
        else {
            moves.push_back(moveBuilder.setDestSquare(singleMoveForwardSquare).build());
            if (pawnRow == initialRow) {
                const auto doubleMoveForwardSquare = pawnSquare + 2 * forward;
                if (board_.isEmpty(doubleMoveForwardSquare)) {
                    moves.push_back(moveBuilder.setDestSquare(doubleMoveForwardSquare).build());
                }
            }
        }
    }

    // Capture
    constexpr std::array<square_t, 2> DELTA_COLUMNS = { DIRECTION_LEFT, DIRECTION_RIGHT };
    for (const auto deltaColumn : DELTA_COLUMNS) {
        auto destColumn = pawnColumn + deltaColumn;
        if (destColumn < 0 || destColumn >= COLUMN_COUNT) {
            continue;
        }
        const auto captureSquare = pawnSquare + forward + deltaColumn;
        if (board_.isNotEmptyAndOtherPlayer(captureSquare, myPlayer)) {
            MoveBuilder mb = moveBuilder
                .setDestSquare(captureSquare)
                .setCapture(board_);
            if (pawnRow == prePromotionRow) {
                generatePromotions(mb, moves);
            }
            else {
                moves.push_back(mb.build());
            }
        }
    }

    // En passant capture
    if (pawnRow == enPassantCaptureRow) {
        const OptionalColumn enPassantColumn = getEnPassantColumn();
        if (enPassantColumn.hasValue()) {
            const auto deltaColumn = std::abs(pawnColumn - enPassantColumn.getColumn());
            if (deltaColumn == 1) {
                const auto captureSquare = encodeSquare(
                    getRow(singleMoveForwardSquare),
                    enPassantColumn.getColumn());
                moves.push_back(
                    moveBuilder
                        .setDestSquare(captureSquare)
                        .setEnPassantCapture()
                        .build());
            }
        }
    }
}

void Position::fillWithKnightMoves(square_t knightSquare, MovesCollection& moves) const
{
    fillWithDeltaMoves(Knight, knightSquare, *this, moves);
}

void Position::fillWithKingMoves(square_t kingSquare, MovesCollection& moves) const
{
    fillWithDeltaMoves(King, kingSquare, *this, moves);
    // TODO: Castle
}

void Position::fillWithSlideMoves(
    piece_type_t pieceType,
    RayIterator rayIterator,
    MovesCollection& moves) const
{
    const player_t myPlayer = getPlayerToMove();
    const square_t origin = rayIterator.currentSquare();
    MoveBuilder moveBuilder{ pieceType, origin };

    while (rayIterator.hasNext()) {
        ++rayIterator;
        const square_t currentSquare = rayIterator.currentSquare();
        MoveBuilder mb = moveBuilder.setDestSquare(currentSquare);
        if (board_.isEmpty(currentSquare)) {
            moves.push_back(mb.build());
        }
        else {
            if (board_.getPlayer(currentSquare) != myPlayer) {
                moves.push_back(mb.setCapture(board_).build());
            }
            break;
        }
    }
}

}
