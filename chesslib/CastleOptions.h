#pragma once

#include "types.h"

namespace chesslib {

struct CastleOptions {
    CastleOptions() : encodedValue_(0) { }

    CastleOptions(position_flags_t encodedValue) : encodedValue_(encodedValue) { }

    bool isCanCastleShort() const
    {
        return encodedValue_ & CAN_CASTLE_SHORT;
    }

    bool isCanCastle() const
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

    position_flags_t getEncodedValue() const
    {
        return encodedValue_;
    }

private:
    static constexpr position_flags_t CAN_CASTLE_SHORT = 1;
    static constexpr position_flags_t CAN_CASTLE_LONG = 2;

    position_flags_t encodedValue_;
};

}
