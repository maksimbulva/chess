#pragma once

#include "Move.h"
#include "types.h"

#include <array>
#include <memory>

namespace chesslib {

class MovesCollection {

    struct ScoredMove {
        friend class MovesCollection;
    public:
        ScoredMove()
            : score_(0)
        {
        }

        Move getMove() const
        {
            return move_;
        }

        evaluation_t getScore() const
        {
            return score_;
        }

    private:
        Move move_;
        evaluation_t score_;
    };

    static constexpr size_t MAX_CAPACITY = 300;
    using Buffer = std::array<ScoredMove, MAX_CAPACITY>;

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

    void clear()
    {
        bufferSize_ = 0;
    }

    Buffer::size_type size() const
    {
        return bufferSize_;
    }

    void pushBack(Move move)
    {
        (*buffer_)[bufferSize_].move_ = move;
        ++bufferSize_;
    }

    void append(const MovesCollection& other);

    Buffer::iterator begin()
    {
        return buffer_->begin();
    }

    Buffer::const_iterator begin() const
    {
        return buffer_->begin();
    }

    Buffer::iterator end()
    {
        return begin() + bufferSize_;
    }

    Buffer::const_iterator end() const
    {
        return begin() + bufferSize_;
    }

    void scoreByMaterialGain();

    void scoreByTableValueDelta(player_t playerToMove);

    static constexpr size_t maxCapacity()
    {
        return MAX_CAPACITY;
    }

private:
    void sortMoves();

    std::unique_ptr<Buffer> buffer_;
    size_t bufferSize_;
};

}
