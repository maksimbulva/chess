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

    void pushBack(Move move)
    {
        (*buffer_)[bufferSize_].move_ = move;
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

    void scoreByMaterialGain();

    void scoreByTableValueDelta(player_t playerToMove);

private:
    void sortMoves();

    std::unique_ptr<Buffer> buffer_;
    size_t bufferSize_;
};

}
