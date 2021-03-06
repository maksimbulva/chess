#include "Engine.h"
#include "notation.h"
#include "require.h"
#include "SearchInfo.h"
#include "Variation.h"

#include <iostream>
#include <sstream>
#include <string>

// TODO: Unbuffered output

namespace chesslib {

void setupGame(std::istringstream& input, Engine& engine)
{
    std::string token;
    input >> std::skipws >> token;

    if (token == "fen") {
        std::string fen;
        while (input >> token) {
            if (token == "moves") {
                break;
            }
            fen += token + " ";
        }
        engine.resetGame(fen);
    }
    else if (token == "startpos") {
        engine.resetGame();
    }
    else {
        FAIL();
    }

    while (input >> token) {
        if (token == "moves") {
            continue;
        }
        const bool isMovePlayed = engine.playMove(token);
        REQUIRE(isMovePlayed);
    }
}

void outputSearchInfo(const SearchInfo& searchInfo)
{
    std::cout << "info"
        << " depth " << searchInfo.bestVariation.getMoves().size()
        << " nodes " << searchInfo.evaluatedPositionCount
        << " nps " << searchInfo.getNodesPerSecond()
        << std::endl;

    std::cout << "info"
        << " score cp " << searchInfo.getEvaluationInCentipawns()
        << " time " << searchInfo.searchTimeMs
        << " pv ";

    for (Move move : searchInfo.bestVariation.getMoves()) {
        std::cout << toCoordinateNotation(move) << " ";
    }
    std::cout << std::endl;
}

void mainLoop(Engine& engine) {
    std::string commandString;

    while (std::getline(std::cin, commandString)) {
        std::istringstream input{ commandString };

        std::string token;
        input >> std::skipws >> token;

        if (token == "quit") {
            break;
        }
        else if (token == "uci") {
            std::cout << "id name " << engine.getName() << std::endl;
            std::cout << "uciok" << std::endl;
        }
        else if (token == "isready") {
            std::cout << "readyok" << std::endl;
        }
        else if (token == "ucinewgame") {
            engine.resetGame();
        }
        else if (token == "position") {
            setupGame(input, engine);
        }
        else if (token == "go") {
            // TODO
            Variation bestVariation = engine.findBestVariation(
                [&engine] (const SearchInfo& searchInfo)
                {
                    outputSearchInfo(engine.getSearchInfo());
                });

            outputSearchInfo(engine.getSearchInfo());

            const Move bestMove = bestVariation.getMoves().empty() ? Move::NullMove() : bestVariation.getMoves()[0];
            std::cout << "bestmove " << toCoordinateNotation(bestMove) << std::endl;
        }
    }
}

}  // namespace chesslib

using namespace chesslib;

int main()
{
    //std::string fenString = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    //std::cout << countLegalMoves(fenString, 4) << std::endl;
    //return 0;

    Engine engine;
    mainLoop(engine);

    return 0;
}
