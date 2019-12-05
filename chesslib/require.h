#pragma once

#include "exceptions.h"

namespace chesslib {

inline void REQUIRE(bool expr)
{
    if (!expr) {
        throw ChesslibException{ };
    }
}

}
