#pragma once

#include "Game.h"
#include "SearchInfo.h"
#include "Variation.h"

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

    Variation findBestVariation();

    SearchInfo getSearchInfo() const
    {
        return searchInfo_;
    }

private:
    Game game_;
    SearchInfo searchInfo_;
};

}