#include "ZobristHasher.h"

#include "Position.h"

#include <limits>
#include <random>

namespace chesslib {

ZobristHasher::ZobristHasher()
{
    std::random_device rndDevice;
    std::mt19937_64 generator(rndDevice());
    std::uniform_int_distribution<uint64_t> distribution;

    for (uint64_t& value : pieceValues_) {
        value = distribution(generator);
    }

    for (uint64_t& value : playerToMoveValues_) {
        value = distribution(generator);
    }

    for (uint64_t& value : enPassantCaptureColumnValues_) {
        value = distribution(generator);
    }

    for (uint64_t& value : castleValues_) {
        value = distribution(generator);
    }
}

position_hash_t ZobristHasher::getValue(const Position& position) const
{
    uint64_t result = getValue(position.getBoard(), Black);
    result ^= getValue(position.getBoard(), White);
    result ^= playerToMoveValues_[position.getPlayerToMove()];
    result ^= enPassantCaptureColumnValues_[position.getEnPassantColumn().getEncodedValue()];
    result ^= getValue(position.getCastleOptions(Black), Black);
    result ^= getValue(position.getCastleOptions(White), White);
    return result;
}

uint64_t ZobristHasher::getValue(const Board& board, player_t player) const
{
    uint64_t result = 0;
    board.doForEachPiece(player, [this, &result, player] (piece_type_t pieceType, square_t square)
        {
            result ^= getValue(player, square, pieceType);
        });
    return result;
}

}
