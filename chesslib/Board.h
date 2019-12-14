#pragma once

#include "PieceOnBoard.h"
#include "types.h"

#include <array>

namespace chesslib {

class Board {
    struct BoardSquare;
    friend class PieceIterator;

public:
    class PieceIterator;

    Board(square_t blackKingSquare, square_t whiteKingSquare);

    bool isEmpty(square_t square) const
    {
        return squares_[square].isEmpty();
    }

    bool isNotEmpty(square_t square) const
    {
        return !isEmpty(square);
    }

    bool isNotEmptyAndOtherPlayer(square_t square, player_t player) const
    {
        const auto squareValue = squares_[square];
        return squareValue.isNotEmpty() && squareValue.getPlayer() != player;
    }

    bool isPlayerPiece(square_t square, player_t player, piece_type_t pieceType) const
    {
        const uint8_t encoded = static_cast<uint8_t>((pieceType << 1) | player);
        return squares_[square].coloredPiece == encoded;
    }

    piece_type_t getPieceTypeAt(square_t square) const
    {
        return squares_[square].getPieceType();
    }

    player_t getPlayer(square_t square) const
    {
        return squares_[square].getPlayer();
    }

    square_t getKingSquare(player_t player) const
    {
        return kingSquares_[player];
    }

    PieceIterator getPieceIterator(player_t player) const
    {
        return PieceIterator(player, *this);
    }

    void addPiece(const PieceOnBoard& piece);

    void updatePieceSquare(const square_t oldSquare, const square_t newSquare);

public:
    class PieceIterator {
        friend class Board;
    public:
        bool hasNext() const
        {
            return current_->nextNode != BoardSquare::NO_NODE;
        }

        void operator++()
        {
            assert(hasNext());
            current_ = &board_->squares_[current_->nextNode];
        }

        piece_type_t getPieceType() const
        {
            return current_->getPieceType();
        }

        player_t getPlayer() const
        {
            return current_->getPlayer();
        }

        square_t getSquare() const
        {
            return current_->square;
        }

    private:
        PieceIterator(player_t player, const Board& board)
            : board_(&board)
            , current_(&board.squares_[board.getKingSquare(player)])
        {
        }

        const Board* const board_;
        const BoardSquare* current_;
    };

private:
    // TODO: pack me
    struct BoardSquare {
        static constexpr uint8_t NO_NODE = 255;

        player_t getPlayer() const
        {
            return static_cast<player_t>(coloredPiece) & 1;
        }

        piece_type_t getPieceType() const
        {
            return static_cast<piece_type_t>(coloredPiece) >> 1;
        }

        void setPiece(player_t player, piece_type_t pieceType)
        {
            coloredPiece = static_cast<uint8_t>(player | (pieceType << 1));
        }

        bool isEmpty() const
        {
            return coloredPiece == 0;
        }

        bool isNotEmpty() const
        {
            return !isEmpty();
        }

        void makeEmpty()
        {
            coloredPiece = 0;
            prevNode = NO_NODE;
            nextNode = NO_NODE;
        }

        uint8_t square = 0;
        uint8_t coloredPiece = 0;
        uint8_t prevNode = NO_NODE;
        uint8_t nextNode = NO_NODE;
    };

private:
    void removeFromList(BoardSquare& square);

    std::array<BoardSquare, SQUARE_COUNT> squares_;
    std::array<square_t, PLAYER_COUNT> kingSquares_;
};

}
