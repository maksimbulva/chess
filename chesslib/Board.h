#pragma once

#include "BoardSquare.h"
#include "PieceOnBoard.h"

#include <array>

namespace chesslib {

class Board {
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

    bool isPlayerSlider(square_t square, player_t player, piece_type_t pieceType) const
    {
        const BoardSquare& boardSquare = squares_[square];
        if (boardSquare.getPlayer() == player) {
            const piece_type_t squarePieceType = boardSquare.getPieceType();
            return squarePieceType == pieceType || squarePieceType == Queen;
        }
        return false;
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

    void erasePieceAt(square_t square);

    void updatePieceSquare(const square_t oldSquare, const square_t newSquare);

    void promotePawn(square_t square, piece_type_t promoteTo);

    void demoteToPawn(square_t square);

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
    void removeFromList(BoardSquare& square);

    std::array<BoardSquare, SQUARE_COUNT> squares_;
    std::array<square_t, PLAYER_COUNT> kingSquares_;
};

}
