#pragma once

#include "types.h"

namespace chesslib {

struct OptionalColumn {
    OptionalColumn() : hasValue_(false), value_(0) { }

    OptionalColumn(square_t column) : hasValue_(true), value_(column) { }

    bool hasValue() const
    {
        return hasValue_;
    }

    square_t getColumn() const
    {
        return value_;
    }

private:
    const bool hasValue_;
    const square_t value_;
};

}
