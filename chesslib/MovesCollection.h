#pragma once

#include "Move.h"

#include <array>
#include <memory>

namespace chesslib {

class MovesCollection {
    static constexpr size_t MAX_CAPACITY = 300;
    using Buffer = std::array<Move, MAX_CAPACITY>;

public:
    MovesCollection()
        : buffer_(std::make_unique<Buffer>())
        , bufferSize_(0)
    {
    }

    bool isEmpty() const
    {
        return bufferSize_ == 0;
    }

    void pushBack(Move move)
    {
        (*buffer_)[bufferSize_] = move;
        ++bufferSize_;
    }

    Buffer::iterator begin()
    {
        return buffer_->begin();
    }

    Buffer::iterator end()
    {
        return begin() + bufferSize_;
    }

private:
    std::unique_ptr<Buffer> buffer_;
    size_t bufferSize_;
};

}
