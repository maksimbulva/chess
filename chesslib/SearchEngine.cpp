#include "SearchEngine.h"

#include "Evaluator.h"
#include "MovesCollection.h"
#include "require.h"

#include <limits>

namespace chesslib {

SearchEngine::SearchEngine(Position position, Evaluator& evaluator)
    : position_(position)
    , evaluator_(&evaluator)
    , searchDepthPly_(0)
{
}

Variation SearchEngine::runSearch(int depthPly)
{
    REQUIRE(depthPly > 0 && depthPly < MovesCollection::maxCapacity());

    searchDepthPly_ = depthPly;
    bestMovesSequence_.clear();

    constexpr evaluation_t beta = std::numeric_limits<evaluation_t>::max();
    constexpr evaluation_t alpha = -beta;

    const evaluation_t sideMultiplier = position_.getPlayerToMove() == Black ? -1 : 1;
    const evaluation_t evaluation =
        sideMultiplier * runAlphaBetaSearch(bestMovesSequence_, depthPly, alpha, beta);

    return Variation(evaluation, bestMovesSequence_);
}

evaluation_t SearchEngine::runAlphaBetaSearch(
    MovesCollection& bestMovesSequence,
    int depthPly,
    evaluation_t alpha,
    evaluation_t beta)
{
    const player_t playerToMove = position_.getPlayerToMove();
    const evaluation_t evaluationSideMultiplier = Evaluator::getSideMultiplier(playerToMove);

    MovesCollection childBestMovesSequence;

    MovesCollection pseudoLegalMoves;
    position_.fillWithPseudoLegalMoves(pseudoLegalMoves, Position::MoveGenerationFilter::AllMoves);
    pseudoLegalMoves.scoreByTableValueDelta(playerToMove);

    Move bestMove = Move::NullMove();
    bool hasLegalMoves = false;

    for (const auto& scoredMove : pseudoLegalMoves) {
        const Move move = scoredMove.getMove();
        position_.playMove(move);
        if (position_.isValid()) {

            hasLegalMoves = true;
 
            evaluation_t evaluation;
            if (depthPly == 1) {
                if (move.isCapture() || move.isPromotion()) {
                    evaluation = -runQuiescentSearch(-beta, -alpha);
                }
                else {
                    evaluation = evaluationSideMultiplier * evaluator_->evaluate(position_);
                }
            }
            else {
                childBestMovesSequence.clear();
                evaluation = -runAlphaBetaSearch(
                    childBestMovesSequence,
                    depthPly - 1,
                    -beta,
                    -alpha);
            }

            if (evaluation >= beta) {
                position_.undoMove();
                return beta;
            } 
            
            if (evaluation > alpha) {
                alpha = evaluation;
                bestMove = move;
                bestMovesSequence.clear();
                bestMovesSequence.pushBack(bestMove);
                bestMovesSequence.append(childBestMovesSequence);
            }
        }
        position_.undoMove();
    }

    if (!hasLegalMoves) {
        const evaluation_t evaluation = evaluator_->evaluateNoLegalMovesPosition(position_);
        if (evaluation > beta) {
            return beta;
        }
        else if (evaluation < alpha) {
            return alpha;
        }
    } 

    return alpha;
}

evaluation_t SearchEngine::runQuiescentSearch(
    evaluation_t alpha,
    evaluation_t beta)
{
    const evaluation_t evaluationSideMultiplier = Evaluator::getSideMultiplier(position_.getPlayerToMove());
    const evaluation_t evaluation = evaluationSideMultiplier * evaluator_->evaluate(position_);

    if (evaluation >= beta) {
        return beta;
    }

    if (evaluation > alpha) {
        alpha = evaluation;
    }

    MovesCollection pseudoLegalMoves;
    position_.fillWithPseudoLegalMoves(pseudoLegalMoves, Position::MoveGenerationFilter::CapturesOnly);
    pseudoLegalMoves.scoreByMaterialGain();

    for (const auto& scoredMove : pseudoLegalMoves) {
        if (!scoredMove.getMove().isCapture()) {
            continue;
        }

        position_.playMove(scoredMove.getMove());
        if (position_.isValid()) {
            evaluation_t subtreeEvaluation = -runQuiescentSearch(-beta, -alpha);

            if (subtreeEvaluation >= beta) {
                position_.undoMove();
                return beta;
            }

            if (subtreeEvaluation > alpha) {
                alpha = subtreeEvaluation;
            }
        }
        position_.undoMove();
    }

    return alpha;
}

}
