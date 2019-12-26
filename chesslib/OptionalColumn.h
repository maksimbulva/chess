#pragma once

#include "types.h"

namespace chesslib {

struct OptionalColumn {
public:
    OptionalColumn() : encoded_(0) { }

    static OptionalColumn fromColumn(square_t column)
    {
        return OptionalColumn(column | FLAG_HAS_VALUE);
    }

    static OptionalColumn fromEncoded(encoded_position_flags_t encoded)
    {
        return OptionalColumn(encoded);
    }

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
    explicit OptionalColumn(encoded_position_flags_t encoded)
        : encoded_(encoded)
    {
    }

    const encoded_position_flags_t encoded_;
};

}
