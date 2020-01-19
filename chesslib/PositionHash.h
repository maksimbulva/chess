#pragma once

#include "types.h"

namespace chesslib {

struct PositionHash {
public:
    PositionHash(position_hash_t hash, position_hash_t flagsHash)
        : hash_(hash)
        , flagsHash_(flagsHash)
    {        
    }

    position_hash_t getHash() const
    {
        return hash_;
    }

    position_hash_t getFlagsHash() const
    {
        return flagsHash_;
    }

private:
    position_hash_t hash_;
    position_hash_t flagsHash_;
};

}
