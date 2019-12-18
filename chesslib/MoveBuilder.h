#pragma once

#include "Board.h"
#include "Move.h"
#include "require.h"
#include "types.h"

namespace chesslib {

struct MoveBuilder {
public:
    MoveBuilder(piece_type_t pieceType, square_t originSquare)
        : encoded_(
            (static_cast<encoded_move_t>(originSquare))
            | ((static_cast<encoded_move_t>(pieceType)) << 12))
    {
        CHECK(pieceType >= Pawn && pieceType <= King);
        CHECK(isValidSquare(originSquare));
    }

    Move build() const
    {
        return Move(encoded_);
    }

    MoveBuilder setDestSquare(square_t destSquare)
    {
        CHECK(isValidSquare(destSquare));
        return MoveBuilder(encoded_ | ((static_cast<encoded_move_t>(destSquare)) << 6));
    }

    MoveBuilder setPromoteToPieceType(piece_type_t promoteTo)
    {
        CHECK(promoteTo >= Knight && promoteTo <= Queen);
        return MoveBuilder(encoded_ | Move::Promotion
            | ((static_cast<encoded_move_t>(promoteTo)) << 15));
    }

    MoveBuilder setCapture(const Board& board)
    {
        const piece_type_t capturedPieceType = board.getPieceTypeAt(getDestSquare());
        CHECK(capturedPieceType >= Pawn && capturedPieceType <= Queen);
        return MoveBuilder(encoded_ | Move::Capture
            | ((static_cast<encoded_move_t>(capturedPieceType)) << 18));
    }

    MoveBuilder setEnPassantCapture()
    {
        constexpr piece_type_t capturedPieceType = Pawn;
        return MoveBuilder(encoded_ | (Move::Capture | Move::EnPassantCapture)
            | ((static_cast<encoded_move_t>(capturedPieceType)) << 18));
    }

    MoveBuilder setPawnDoubleMove()
    {
        return MoveBuilder(encoded_ | Move::PawnDoubleMove);
    }

private:
    MoveBuilder(const encoded_move_t encoded)
        : encoded_(encoded)
    {
    }

    square_t getOriginSquare() const
    {
        return encoded_ & SQUARE_MASK;
    }

    square_t getDestSquare() const
    {
        return (encoded_ >> 6) & SQUARE_MASK;
    }

    encoded_move_t encoded_;
};

}