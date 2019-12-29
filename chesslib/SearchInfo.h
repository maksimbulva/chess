#pragma once

#include "Variation.h"

#include <cstdint>

namespace chesslib {

struct SearchInfo {
    uint64_t getNodesPerSecond() const
    {
        return searchTimeMs >= 1000 ? searchTreeSize * 1000 / searchTimeMs : searchTreeSize;
    }

    Variation bestVariation;
    uint64_t searchTreeSize;
    int64_t searchTimeMs;
};

}
