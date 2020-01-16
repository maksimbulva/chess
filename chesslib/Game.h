#pragma once

#include "MemoryPool.h"
#include "Position.h"

#include <vector>

namespace chesslib {

class Game {
public:
    explicit Game(const Position& position);

    const Position& getCurrentPosition() const
    {
        return currentPosition_;
    }

    const std::vector<Move>& getLegalMoves() const
    {
        return legalMoves_;
    }

    Move getRandomMove() const;

    void playMove(Move move);

private:
    void updateLegalMoves();

    Position initialPosition_;
    Position currentPosition_;

    MemoryPool memoryPool_;
    std::vector<Move> legalMoves_;
};

}
