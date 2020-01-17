#pragma once

#include "types.h"

#include <unordered_map>

namespace chesslib {

class TranspositionTable {
public:
    struct Value {

    };

public:
    TranspositionTable();

    bool insert(position_hash_t key)
    {
        if (isNotFull()) {
            table_[key] = Value();
            return true;
        }
        else {
            return false;
        }
    }

    bool isNotFull() const
    {
        return table_.size() < MAX_ELEMENT_COUNT;
    }

private:
    static constexpr size_t MAX_ELEMENT_COUNT = 1 << 20;

    std::unordered_map<position_hash_t, Value> table_;
};

}
