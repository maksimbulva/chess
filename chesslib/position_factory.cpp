#include "position_factory.h"

#include "require.h"

#include <algorithm>

namespace chesslib {

Position createPosition(
    std::vector<PieceOnBoard> pieces,
    player_t playerToMove,
    OptionalColumn enPassantColumn,
    CastleOptions whiteCastleOptions,
    CastleOptions blackCastleOptions,
    uint32_t halfmoveClock,
    uint32_t fullmoveNumber)
{
    auto blackKingIt = std::find_if(pieces.begin(), pieces.end(),
        [] (const auto& piece) { return piece.pieceType == King && piece.player == Black; });

    auto whiteKingIt = std::find_if(pieces.begin(), pieces.end(),
        [] (const auto& piece) { return piece.pieceType == King && piece.player == White; });

    REQUIRE(blackKingIt != pieces.end() && whiteKingIt != pieces.end());

    auto position = Position(
        blackKingIt->square,
        whiteKingIt->square,
        playerToMove,
        halfmoveClock,
        fullmoveNumber);

    for (const auto& piece : pieces) {
        if (piece.pieceType != King) {
            position.addPiece(piece);
        }
    }

    position.setEnPassantColumn(enPassantColumn);
    position.setCastleOptions(White, whiteCastleOptions);
    position.setCastleOptions(Black, blackCastleOptions);

    position.optimizeCastleOptions();

    REQUIRE(position.isValid());
    return position;
}

}
