#include "position_factory.h"

#include "require.h"

#include <algorithm>

namespace chesslib {

Position createPosition(std::vector<PieceOnBoard> pieces)
{
    auto blackKingIt = std::find_if(pieces.begin(), pieces.end(),
        [] (const auto& piece) { return piece.pieceType == King && piece.player == Black; });

    auto whiteKingIt = std::find_if(pieces.begin(), pieces.end(),
        [] (const auto& piece) { return piece.pieceType == King && piece.player == White; });

    REQUIRE(blackKingIt != pieces.end() && whiteKingIt != pieces.end());

    auto position = Position(blackKingIt->square, whiteKingIt->square);

    for (const auto& piece : pieces) {
        if (piece.pieceType != King) {
            position.addPiece(piece);
        }
    }

    REQUIRE(!position.isKingCanBeCaptured());
    return position;
}

}
