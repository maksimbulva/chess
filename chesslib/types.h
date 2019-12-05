#pragma once

#include <cstdint>

namespace chesslib {

using fastint = int_fast32_t;

using piece_type_t = fastint;
using player_t = fastint;
using square_t = fastint;
using row_t = fastint;
using column_t = fastint;

enum PieceType : piece_type_t {
    NoPiece = 0,
    Pawn,
    Knight,
    Bishop,
    Rook,
    Queen,
    King
};

enum Player : player_t {
    Black = 0,
    White
};

constexpr size_t PLAYER_COUNT = 2;
constexpr size_t SQUARE_COUNT = 64;

constexpr square_t ROW_COUNT = 8;
constexpr square_t COLUMN_COUNT = 8;


}  // chesslib
