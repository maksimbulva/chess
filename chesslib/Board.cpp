#include "Board.h"
#include "require.h"

namespace chesslib {

Board::Board(square_t blackKingSquare, square_t whiteKingSquare)
    : kingSquares_({blackKingSquare, whiteKingSquare})
{
    assert(blackKingSquare != whiteKingSquare);

    for (std::size_t i = 0; i < squares_.size(); ++i) {
        squares_[i].square = static_cast<uint8_t>(i);
    }

    squares_[blackKingSquare].setPiece(Black, King);
    squares_[whiteKingSquare].setPiece(White, King);
}

void Board::addPiece(const PieceOnBoard& piece)
{
    assert(piece.pieceType != King && piece.pieceType != NoPiece);
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

void Board::erasePieceAt(square_t square)
{
    BoardSquare& boardSquare = squares_[square];
    removeFromList(boardSquare);
    boardSquare.makeEmpty();
}

void Board::updatePieceSquare(const square_t oldSquare, const square_t newSquare)
{
    BoardSquare& oldBoardSquare = squares_[oldSquare];
    BoardSquare& newBoardSquare = squares_[newSquare];

    CHECK(oldBoardSquare.isNotEmpty() && newBoardSquare.isEmpty());

    newBoardSquare.coloredPiece = oldBoardSquare.coloredPiece;
    newBoardSquare.prevNode = oldBoardSquare.prevNode;
    newBoardSquare.nextNode = oldBoardSquare.nextNode;

    if (oldBoardSquare.prevNode != BoardSquare::NO_NODE) {
        squares_[oldBoardSquare.prevNode].nextNode = newSquare;
    }
    if (oldBoardSquare.nextNode != BoardSquare::NO_NODE) {
        squares_[oldBoardSquare.nextNode].prevNode = newSquare;
    }
    oldBoardSquare.makeEmpty();
}

void Board::removeFromList(BoardSquare& square)
{
    if (square.prevNode != BoardSquare::NO_NODE) {
        squares_[square.prevNode].nextNode = square.nextNode;
    }
    if (square.nextNode != BoardSquare::NO_NODE) {
        squares_[square.nextNode].prevNode = square.prevNode;
    }
}

}