#pragma once

#include "types.h"

namespace chesslib {

struct OptionalColumn {
    OptionalColumn() : encoded_(0) { }

    OptionalColumn(square_t column) : encoded_(column | FLAG_HAS_VALUE) { }

    bool hasValue() const
    {
        return encoded_ & FLAG_HAS_VALUE;
    }

    square_t getColumn() const
    {
        return static_cast<square_t>(encoded_ & COLUMN_MASK);
    }

    encoded_position_flags_t getEncodedValue() const
    {
        return encoded_;
    }

private:
    static constexpr encoded_position_flags_t COLUMN_MASK = 7;
    static constexpr encoded_position_flags_t FLAG_HAS_VALUE = 8;

private:
    const encoded_position_flags_t encoded_;
};

}
