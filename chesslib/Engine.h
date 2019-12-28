#pragma once

#include "Game.h"
#include "Move.h"

#include <string>

namespace chesslib {

class Engine {
public:
    Engine();

    std::string getName() const;

    Game& getGame()
    {
        return game_;
    }

    void resetGame();

    bool playMove(square_t originSquare, square_t destSquare);

    Move findBestMove();

private:
    Game game_;
};

}