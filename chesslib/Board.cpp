#include "Board.h"
#include "require.h"

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

void Board::addPiece(const PieceOnBoard& piece)
{
    _ASSERT(piece.pieceType != King && piece.pieceType != NoPiece);
    REQUIRE(isEmpty(piece.square));

    auto& kingNode = squares_[getKingSquare(piece.player)];
    auto& pieceNode = squares_[piece.square];

    pieceNode.setPiece(piece.player, piece.pieceType);
    pieceNode.nextNode = kingNode.nextNode;
    pieceNode.prevNode = kingNode.square;

    if (kingNode.nextNode != BoardSquare::NO_NODE) {
        auto& kingNextNode = squares_[kingNode.nextNode];
        kingNextNode.prevNode = piece.square;
    }

    kingNode.nextNode = piece.square;
}

}