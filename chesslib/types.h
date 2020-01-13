#pragma once

#include <cassert>
#include <cstddef>
#include <cstdint>

namespace chesslib {

using fastint = int_fast32_t;

using piece_type_t = fastint;
using player_t = fastint;
using square_t = fastint;
using row_t = fastint;
using column_t = fastint;

using encoded_move_t = uint32_t;

static constexpr encoded_move_t SQUARE_MASK = 63;

using encoded_position_flags_t = uint32_t;

using evaluation_t = int32_t;

enum PieceType : piece_type_t {
    NoPiece = 0,
    Pawn,
    Knight,
    Bishop,
    Rook,
    Queen,
    King
};

constexpr player_t Black = 0;
constexpr player_t White = 1;

constexpr player_t getOtherPlayer(player_t player)
{
    return 1 - player;
}

constexpr std::size_t PLAYER_COUNT = 2;
constexpr std::size_t SQUARE_COUNT = 64;

constexpr square_t WHITE_PAWN_ROW = 1;
constexpr square_t BLACK_PAWN_ROW = 6;

constexpr square_t ROW_COUNT = 8;
constexpr square_t COLUMN_COUNT = 8;

constexpr square_t MAX_ROW = ROW_COUNT - 1;
constexpr square_t MAX_COLUMN = COLUMN_COUNT - 1;

inline constexpr bool isValidSquare(square_t square)
{
    return square >= 0 && square < 64;
}

constexpr inline square_t encodeSquare(square_t row, square_t column)
{
    assert(row >= 0 && row < ROW_COUNT && column >= 0 && column < COLUMN_COUNT);
    return (row << 3) | column;
}

constexpr inline square_t getRow(square_t square)
{
    return square >> 3;
}

constexpr inline square_t getColumn(square_t square)
{
    return square & 7;
}

}  // chesslib
