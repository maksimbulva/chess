#include "Board.h"

namespace chesslib {

Board::Board(square_t blackKingSquare, square_t whiteKingSquare)
    : kingSquares_({blackKingSquare, whiteKingSquare})
{
    _ASSERT(blackKingSquare != whiteKingSquare);

    for (size_t i = 0; i < squares_.size(); ++i) {
        squares_[i].square = static_cast<uint8_t>(i);
    }

    squares_[blackKingSquare].setPiece(Black, King);
    squares_[whiteKingSquare].setPiece(White, King);
}

}