#include "Engine.h"

#include "Evaluator.h"
#include "fen.h"
#include "SearchEngine.h"
#include "Stopwatch.h"

namespace chesslib {

namespace {

const char* const initialPositonFen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

}   // namespace

Engine::Engine()
    : game_(decodeFen(initialPositonFen))
{
}

std::string Engine::getName() const
{
    return "Chesslib v0.01";
}

void Engine::resetGame()
{
    resetGame(initialPositonFen);
}

void Engine::resetGame(const std::string& positionFen)
{
    game_ = Game{ decodeFen(positionFen) };
}

bool Engine::playMove(square_t originSquare, square_t destSquare, piece_type_t promoteToPieceType)
{
    const auto& legalMoves = game_.getLegalMoves();
    const auto moveIter = std::find_if(legalMoves.begin(), legalMoves.end(),
        [originSquare, destSquare, promoteToPieceType] (const Move& move)
        {
            return move.getOriginSquare() == originSquare
                && move.getDestSquare() == destSquare
                && ((move.isPromotion() && promoteToPieceType == move.getPromoteToPieceType())
                    || (!move.isPromotion() && promoteToPieceType == NoPiece));
        });

    if (moveIter != legalMoves.end()) {
        game_.playMove(*moveIter);
        return true;
    }
    else {
        return false;
    }
}

Variation Engine::findBestVariation(
    std::function<void(const SearchInfo & searchInfo)> progressCallback)
{
    Stopwatch stopwatch;

    const Position& currentPosition = game_.getCurrentPosition();
    searchInfo_.playerToMove = currentPosition.getPlayerToMove();

    Evaluator evaluator;
    const uint64_t maxEvaluations = 5000000;

    SearchEngine searchEngine(currentPosition, evaluator, maxEvaluations);

    for (int depthPly = 2; ; ++depthPly) {
        auto searchResult = searchEngine.runSearch(depthPly);
        if (searchEngine.isSearchAborted()
            || std::abs(searchResult.getEvaluation()) >= Evaluator::GoodEnoughToStopIterativeDeepening) {
            break;
        }

        searchInfo_.bestVariation = std::move(searchResult);
        searchInfo_.evaluatedPositionCount = evaluator.getEvaluatedPositionCount();
        searchInfo_.searchTimeMs = stopwatch.getElapsedMilliseconds();

        if (progressCallback) {
            progressCallback(searchInfo_);
        }
    } 

    return searchInfo_.bestVariation;
}

}
