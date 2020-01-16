#pragma once

#include "types.h"

namespace chesslib {

struct CastleOptions {
    CastleOptions() : encodedValue_(0) { }

    explicit constexpr CastleOptions(encoded_position_flags_t encodedValue)
        : encodedValue_(encodedValue)
    {
    }

    bool isCannotCastle() const
    {
        return encodedValue_ == 0;
    }

    bool isCanCastleShort() const
    {
        return encodedValue_ & CAN_CASTLE_SHORT;
    }

    bool isCanCastleLong() const
    {
        return encodedValue_ & CAN_CASTLE_LONG;
    }

    void setCanCastleShort(bool value)
    {
        if (value) {
            encodedValue_ |= CAN_CASTLE_SHORT;
        }
        else {
            encodedValue_ &= ~CAN_CASTLE_SHORT;
        }
    }

    void setCanCastleLong(bool value)
    {
        if (value) {
            encodedValue_ |= CAN_CASTLE_LONG;
        }
        else {
            encodedValue_ &= ~CAN_CASTLE_LONG;
        }
    }

    encoded_position_flags_t getEncodedValue() const
    {
        return encodedValue_;
    }

public:
    static constexpr size_t MAX_ENCODED_VALUE = 3;

private:
    static constexpr encoded_position_flags_t CAN_CASTLE_SHORT = 1;
    static constexpr encoded_position_flags_t CAN_CASTLE_LONG = 2;

    encoded_position_flags_t encodedValue_;
};

}
