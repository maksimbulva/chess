#pragma once

#include "CastleOptions.h"
#include "OptionalColumn.h"
#include "types.h"

#include <array>

namespace chesslib {

class Board;
class Position;

class ZobristHasher {
public:
    ZobristHasher();

    uint64_t getValue(player_t player, square_t square, piece_type_t pieceType) const
    {
        const size_t index = ((((pieceType - 1) << 1) | player) << 6) | square;
        return pieceValues_[index];
    }

    position_hash_t getValue(const Position& position) const;

private:
    uint64_t getValue(const Board& board, player_t player) const;

    uint64_t getValue(const CastleOptions& castleOptions, player_t player) const
    {
        const size_t index = (castleOptions.getEncodedValue() << 1) | player;
        return castleValues_[index];
    }

private:
    static constexpr size_t PIECE_VALUES_SIZE = King * PLAYER_COUNT * SQUARE_COUNT;
    static constexpr size_t CASTLE_VALUES_SIZE = (CastleOptions::MAX_ENCODED_VALUE + 1) * PLAYER_COUNT;

    std::array<uint64_t, PIECE_VALUES_SIZE> pieceValues_;
    std::array<uint64_t, PLAYER_COUNT> playerToMoveValues_;
    std::array<uint64_t, OptionalColumn::MAX_ENCODED_VALUE + 1> enPassantCaptureColumnValues_;
    std::array<uint64_t, CASTLE_VALUES_SIZE> castleValues_;
};

}
