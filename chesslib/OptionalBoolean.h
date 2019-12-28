#pragma once

#include "types.h"

namespace chesslib {

struct OptionalBoolean {
public:
    OptionalBoolean() : encoded_(0) { }

    explicit OptionalBoolean(bool value)
        : encoded_(FLAG_HAS_VALUE | (value ? 1 : 0))
    {
    }

    explicit OptionalBoolean(encoded_position_flags_t encoded)
        : encoded_(encoded)
    {
    }

    bool hasValue() const
    {
        return encoded_ & FLAG_HAS_VALUE;
    }

    bool getValue() const
    {
        return encoded_ & VALUE_MASK;
    }

    bool isEquals(bool value) const
    {
        return encoded_ == (FLAG_HAS_VALUE | (value ? 1 : 0));
    }

    encoded_position_flags_t getEncodedValue() const
    {
        return encoded_;
    }

private:
    static constexpr encoded_position_flags_t VALUE_MASK = 1;
    static constexpr encoded_position_flags_t FLAG_HAS_VALUE = 2;

private:
    const encoded_position_flags_t encoded_;
};

}
