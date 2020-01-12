#pragma once

#include "types.h"

namespace chesslib {

struct Move {
public:

#pragma warning(push)
#pragma warning(disable:26495)
    // Default ctor for use in MovesCollection only
    Move()
    {
    }
#pragma warning(pop)

    // Use MoveBuilder class to get encoded value
    constexpr explicit Move(const encoded_move_t encoded)
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

    piece_type_t getPieceType() const
    {
        return static_cast<piece_type_t>((encoded_ >> 12) & PIECE_TYPE_MASK);
    }

    piece_type_t getPromoteToPieceType() const
    {
        return static_cast<piece_type_t>((encoded_ >> 15) & PIECE_TYPE_MASK);
    }

    piece_type_t getCapturedPieceType() const
    {
        return static_cast<piece_type_t>((encoded_ >> 18) & PIECE_TYPE_MASK);
    }

    bool isCapture() const
    {
        return encoded_ & Capture;
    }

    bool isEnPassantCapture() const
    {
        return encoded_ & EnPassantCapture;
    }

    bool isPromotion() const
    {
        return encoded_ & Promotion;
    }

    bool isShortCastle() const
    {
        return encoded_ & ShortCastle;
    }

    bool isLongCastle() const
    {
        return encoded_ & LongCastle;
    }

    bool isCastle() const
    {
        return encoded_ & AnyCastle;
    }

    bool isPawnDoubleMove() const
    {
        return encoded_ & PawnDoubleMove;
    }

    bool isNullMove() const
    {
        return encoded_ == 0;
    }

public:
    static constexpr encoded_move_t Capture = (static_cast<encoded_move_t>(1)) << 21;
    static constexpr encoded_move_t EnPassantCapture = (static_cast<encoded_move_t>(1)) << 22;
    static constexpr encoded_move_t Promotion = (static_cast<encoded_move_t>(1)) << 23;
    static constexpr encoded_move_t ShortCastle = (static_cast<encoded_move_t>(1)) << 24;
    static constexpr encoded_move_t LongCastle = (static_cast<encoded_move_t>(1)) << 25;
    static constexpr encoded_move_t PawnDoubleMove = (static_cast<encoded_move_t>(1)) << 26;

    static constexpr encoded_move_t AnyCastle = ShortCastle | LongCastle;

    static constexpr Move NullMove()
    {
        return Move(0);
    }

private:
    encoded_move_t encode(piece_type_t pieceType, square_t originSquare, square_t destSquare)
    {
        return static_cast<encoded_move_t>(originSquare | (destSquare << 6) | (pieceType << 12));
    }

    static constexpr encoded_move_t PIECE_TYPE_MASK = 7;

    /**
     * bits 0..5 - move origin square
     * bits 6..11 - move destination square
     * bits 12..14 - piece type to move
     * bits 15..17 - promote to piece type (pawn promotion)
     * bits 18..20 - captured piece type (for move undos)
     */ 
    encoded_move_t encoded_;
};

}
