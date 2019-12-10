#include "Position.h"

namespace chesslib {

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

}
