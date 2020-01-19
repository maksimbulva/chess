#pragma once

#include "Game.h"
#include "SearchInfo.h"
#include "Variation.h"

#include <functional>
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

    bool playMove(
        square_t originSquare,
        square_t destSquare,
        piece_type_t promoteToPieceType = NoPiece);

    Variation findBestVariation(
        std::function<void(const SearchInfo& searchInfo)> progressCallback);

    SearchInfo getSearchInfo() const
    {
        return searchInfo_;
    }

private:
    Game game_;
    SearchInfo searchInfo_;
};

}