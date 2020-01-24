#pragma once

#include "types.h"

namespace chesslib {

enum Rank : square_t {
    ROW_1 = 0,
    ROW_2,
    ROW_3,
    ROW_4,
    ROW_5,
    ROW_6,
    ROW_7,
    ROW_8
};

enum Columns : square_t {
    COLUMN_A = 0,
    COLUMN_B,
    COLUMN_C,
    COLUMN_D,
    COLUMN_E,
    COLUMN_F,
    COLUMN_G,
    COLUMN_H
};

enum Squares : square_t {
    A1 = encodeSquare(ROW_1, COLUMN_A),
    D1 = encodeSquare(ROW_1, COLUMN_D),
    F1 = encodeSquare(ROW_1, COLUMN_F),
    H1 = encodeSquare(ROW_1, COLUMN_H),
    A8 = encodeSquare(ROW_8, COLUMN_A),
    D8 = encodeSquare(ROW_8, COLUMN_D),
    F8 = encodeSquare(ROW_8, COLUMN_F),
    H8 = encodeSquare(ROW_8, COLUMN_H)
};

}
