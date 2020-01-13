#pragma once

#include "types.h"

namespace chesslib {

constexpr square_t ROW_1 = 0;

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

}
