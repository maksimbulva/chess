#include "ZobristHasher.h"

#include "Position.h"
#include "Random.h"

#include <limits>

namespace chesslib {

ZobristHasher::ZobristHasher()
{
    Random random;

    for (uint64_t& value : pieceValues_) {
        value = random.getNextU64();
    }

    for (uint64_t& value : playerToMoveValues_) {
        value = random.getNextU64();
    }

    for (uint64_t& value : enPassantCaptureColumnValues_) {
        value = random.getNextU64();
    }

    for (uint64_t& value : castleValues_) {
        value = random.getNextU64();
    }
}

PositionHash ZobristHasher::getValue(const Position& position) const
{
    const uint64_t flagsHash = getValue(position.getPositionFlags());
    uint64_t hash = getValue(position.getBoard(), Black);
    hash ^= flagsHash;
    return PositionHash(hash, flagsHash);
}

position_hash_t ZobristHasher::getValue(PositionFlags flags) const
{
    uint64_t result = playerToMoveValues_[flags.getPlayerToMove()];
    result ^= enPassantCaptureColumnValues_[flags.getEnPassantColumn().getEncodedValue()];
    result ^= getValue(flags.getCastleOptions(Black), Black);
    result ^= getValue(flags.getCastleOptions(White), White);
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
