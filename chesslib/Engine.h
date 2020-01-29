#pragma once

#include "Game.h"
#include "Player.h"
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

    Player& getPlayer(player_t player)
    {
        return players_[player];
    }

    void resetGame();

    void resetGame(const std::string& positionFen);

    bool playMove(
        square_t originSquare,
        square_t destSquare,
        piece_type_t promoteToPieceType = NoPiece);

    bool playMove(const std::string& moveString);

    Variation findBestVariation(
        std::function<void(const SearchInfo& searchInfo)> progressCallback);

    SearchInfo getSearchInfo() const
    {
        return searchInfo_;
    }

private:
    Game game_;
    std::array<Player, PLAYER_COUNT> players_;
    SearchInfo searchInfo_;
};

}