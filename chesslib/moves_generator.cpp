#include "MoveBuilder.h"
#include "Position.h"

#include "bitboard.h"
#include "require.h"
#include "squares.h"

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

void fillWithDeltaMoves(
    piece_type_t pieceType,
    square_t origin,
    const Position& position,
    MovesCollection& moves,
    Position::MoveGenerationFilter movesFilter)
{
    const auto& deltas = pieceType == Knight ? KNIGHT_MOVE_DELTAS : KING_MOVE_DELTAS;
    const player_t myPlayer = position.getPlayerToMove();
    const Board& board = position.getBoard();
    const bitboard_t originBit = setSquare(0, origin);
    MoveBuilder moveBuilder{ pieceType, origin };

    if (movesFilter == Position::MoveGenerationFilter::AllMoves) {
        for (const auto& delta : deltas) {
            if (delta.origins & originBit) {
                const square_t destSquare = origin + delta.delta;
                MoveBuilder mb = moveBuilder.setDestSquare(destSquare);
                if (board.isEmpty(destSquare)) {
                    moves.pushBack(mb.build());
                }
                else if (board.getPlayer(destSquare) != myPlayer) {
                    moves.pushBack(mb.setCapture(board).build());
                }
            }
        }
    }
    else {
        for (const auto& delta : deltas) {
            const square_t destSquare = origin + delta.delta;
            if ((delta.origins & originBit) && board.isNotEmpty(destSquare)
                && board.getPlayer(destSquare) != myPlayer) {
                moves.pushBack(moveBuilder
                    .setDestSquare(destSquare)
                    .setCapture(board)
                    .build());
            }
        }
    }
}

}  // namespace

MemoryPool::PooledPtr Position::generatePseudoLegalMoves(
    MoveGenerationFilter movesFilter,
    MemoryPool& memoryPool)
{
    auto movesCollection = memoryPool.getMovesCollection();
    fillWithPseudoLegalMoves(*movesCollection, movesFilter, isInCheck());
    return movesCollection;
}

void Position::fillWithPseudoLegalMoves(
    MovesCollection& moves,
    MoveGenerationFilter movesFilter,
    bool isInCheck) const
{
    assert(moves.isEmpty());

    const player_t myPlayer = getPlayerToMove();
    auto myPiecesIt = board_.getPieceIterator(myPlayer);

    while (true) {

        const square_t origin = myPiecesIt.getSquare();
        const piece_type_t pieceType = myPiecesIt.getPieceType();
        switch (pieceType)
        {
        case Pawn:
            fillWithPawnMoves(origin, moves, movesFilter);
            break;
        case Knight:
            fillWithKnightMoves(origin, moves, movesFilter);
            break;
        case King:
            fillWithKingMoves(origin, moves, movesFilter, isInCheck);
            break;
        default:
            if (pieceType == Rook || pieceType == Queen) {
                fillWithSlideMoves(pieceType, createRayIterator<DIRECTION_LEFT>(origin), moves, movesFilter);
                fillWithSlideMoves(pieceType, createRayIterator<DIRECTION_RIGHT>(origin), moves, movesFilter);
                fillWithSlideMoves(pieceType, createRayIterator<DIRECTION_DOWN>(origin), moves, movesFilter);
                fillWithSlideMoves(pieceType, createRayIterator<DIRECTION_UP>(origin), moves, movesFilter);
            }
            if (pieceType == Bishop || pieceType == Queen) {
                fillWithSlideMoves(pieceType, createRayIterator<DIRECTION_DOWN_LEFT>(origin), moves, movesFilter);
                fillWithSlideMoves(pieceType, createRayIterator<DIRECTION_DOWN_RIGHT>(origin), moves, movesFilter);
                fillWithSlideMoves(pieceType, createRayIterator<DIRECTION_UP_LEFT>(origin), moves, movesFilter);
                fillWithSlideMoves(pieceType, createRayIterator<DIRECTION_UP_RIGHT>(origin), moves, movesFilter);
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
        moves.pushBack(moveBuilder.setPromoteToPieceType(promotionPiece).build());
    }
}

}  // namespace

void Position::fillWithPawnMoves(
    square_t pawnSquare,
    MovesCollection& moves,
    MoveGenerationFilter movesFilter) const
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
    if (movesFilter == MoveGenerationFilter::AllMoves) {
        if (board_.isEmpty(singleMoveForwardSquare)) {
            if (pawnRow == prePromotionRow) {
                generatePromotions(moveBuilder.setDestSquare(singleMoveForwardSquare), moves);
            }
            else {
                moves.pushBack(moveBuilder.setDestSquare(singleMoveForwardSquare).build());
                if (pawnRow == initialRow) {
                    const auto doubleMoveForwardSquare = pawnSquare + 2 * forward;
                    if (board_.isEmpty(doubleMoveForwardSquare)) {
                        moves.pushBack(
                            moveBuilder.setDestSquare(doubleMoveForwardSquare)
                            .setPawnDoubleMove()
                            .build());
                    }
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
                moves.pushBack(mb.build());
            }
        }
    }

    // En passant capture
    if (pawnRow == enPassantCaptureRow) {
        const OptionalColumn enPassantColumn = getEnPassantColumn();
        if (enPassantColumn.hasValue()) {
            const auto enemyPawnColumn = enPassantColumn.getColumn();
            const auto deltaColumn = std::abs(pawnColumn - enemyPawnColumn);
            if (deltaColumn == 1) {
                const auto enemyPawnSquare = encodeSquare(pawnRow, enemyPawnColumn);
                if (board_.isPlayerPiece(enemyPawnSquare, getOtherPlayer(), Pawn)) {
                    const auto captureSquare = encodeSquare(
                        getRow(singleMoveForwardSquare),
                        enPassantColumn.getColumn());
                    moves.pushBack(
                        moveBuilder
                            .setDestSquare(captureSquare)
                            .setEnPassantCapture()
                            .build());
                }
            }
        }
    }
}

void Position::fillWithKnightMoves(
    square_t knightSquare,
    MovesCollection& moves,
    MoveGenerationFilter movesFilter) const
{
    fillWithDeltaMoves(Knight, knightSquare, *this, moves, movesFilter);
}

void Position::fillWithKingMoves(
    square_t kingSquare,
    MovesCollection& moves,
    MoveGenerationFilter movesFilter,
    bool isInCheck) const
{
    fillWithDeltaMoves(King, kingSquare, *this, moves, movesFilter);

    if (movesFilter == MoveGenerationFilter::CapturesOnly) {
        return;
    }

    const player_t otherPlayer = getOtherPlayer();

    // Castle
    const CastleOptions castleOptions = getCastleOptions(getPlayerToMove());
    if (castleOptions.isCannotCastle() || isInCheck) {
        return;
    }

    MoveBuilder moveBuilder{ King, kingSquare };

    const square_t kingRow = getRow(kingSquare);

    if (castleOptions.isCanCastleLong()) {
        bool isEmptySquares = true;
        for (square_t column = COLUMN_B; column < COLUMN_E; ++column) {
            if (board_.isNotEmpty(encodeSquare(kingRow, column))) {
                isEmptySquares = false;
                break;
            }
        }
        if (isEmptySquares
            && !isSquareAttacked(encodeSquare(kingRow, COLUMN_D), board_, otherPlayer)) {
            moves.pushBack(
                moveBuilder
                .setDestSquare(encodeSquare(kingRow, COLUMN_C))
                .setLongCastle()
                .build());
        }
    }

    if (castleOptions.isCanCastleShort()) {
        bool isEmptySquares = true;
        for (square_t column = COLUMN_F; column < COLUMN_H; ++column) {
            if (board_.isNotEmpty(encodeSquare(kingRow, column))) {
                isEmptySquares = false;
                break;
            }
        }
        if (isEmptySquares
            && !isSquareAttacked(encodeSquare(kingRow, COLUMN_F), board_, otherPlayer)) {
            moves.pushBack(
                moveBuilder
                .setDestSquare(encodeSquare(kingRow, COLUMN_G))
                .setShortCastle()
                .build());
        }
    }
}

void Position::fillWithSlideMoves(
    piece_type_t pieceType,
    RayIterator rayIterator,
    MovesCollection& moves,
    MoveGenerationFilter movesFilter) const
{
    const player_t myPlayer = getPlayerToMove();
    const square_t origin = rayIterator.currentSquare();
    MoveBuilder moveBuilder{ pieceType, origin };

    if (movesFilter == MoveGenerationFilter::AllMoves) {
        while (rayIterator.hasNext()) {
            ++rayIterator;
            const square_t currentSquare = rayIterator.currentSquare();
            MoveBuilder mb = moveBuilder.setDestSquare(currentSquare);
            if (board_.isEmpty(currentSquare)) {
                moves.pushBack(mb.build());
            }
            else {
                if (board_.getPlayer(currentSquare) != myPlayer) {
                    moves.pushBack(mb.setCapture(board_).build());
                }
                break;
            }
        }
    }
    else {
        while (rayIterator.hasNext()) {
            ++rayIterator;
            const square_t currentSquare = rayIterator.currentSquare();
            if (board_.isNotEmpty(currentSquare)) {
                if (board_.getPlayer(currentSquare) != myPlayer) {
                    moves.pushBack(moveBuilder
                        .setDestSquare(currentSquare)
                        .setCapture(board_)
                        .build());
                }
                break;
            }
        }
    }
}

}
