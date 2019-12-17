#pragma once

#include "exceptions.h"

namespace chesslib {

inline void FAIL()
{
    throw ChesslibException{ };
}
    
inline void REQUIRE(bool expr)
{
    if (!expr) {
        FAIL();
    }
}

#define CHECK(expr) REQUIRE(expr)

}
