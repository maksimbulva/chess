#include "SearchEngine.h"

#include "Evaluator.h"
#include "MovesCollection.h"
#include "require.h"
#include "SearchTree.h"

#include <limits>

namespace chesslib {

SearchEngine::SearchEngine(Position position, Evaluator& evaluator)
    : position_(position)
    , evaluator_(&evaluator)
{
}

Variation SearchEngine::runSearch(int depthPly)
{
    REQUIRE(depthPly > 0);

    constexpr evaluation_t beta = std::numeric_limits<evaluation_t>::max();
    constexpr evaluation_t alpha = -beta;

    SearchTree searchTree(position_);
    const evaluation_t evaluation = runAlphaBetaSearch(searchTree.getRoot(), depthPly, alpha, beta);

    return searchTree.getBestVariation();
}

evaluation_t SearchEngine::runAlphaBetaSearch(
    SearchNode& parent,
    int depthPly,
    evaluation_t alpha,
    evaluation_t beta)
{
    const player_t playerToMove = position_.getPlayerToMove();
    const evaluation_t evaluationSideMultiplier = Evaluator::getSideMultiplier(playerToMove);

    MovesCollection pseudoLegalMoves;
    position_.fillWithPseudoLegalMoves(pseudoLegalMoves, Position::MoveGenerationFilter::AllMoves);
    pseudoLegalMoves.scoreByTableValueDelta(playerToMove);

    Move bestMove = Move::NullMove();
    bool hasLegalMoves = false;

    SearchNodeRef bestSubtreeRoot;
    SearchNodeRef currentSubtreeRoot;

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
                currentSubtreeRoot = SearchNode::createRef(move);
                evaluation = -runAlphaBetaSearch(
                    *currentSubtreeRoot,
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
                bestSubtreeRoot = std::move(currentSubtreeRoot);
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

    evaluation_t nodeEvaluation = evaluationSideMultiplier * alpha;
    if (depthPly == 1) {
        if (!bestMove.isNullMove()) {
            parent.setChild(SearchNode::createRef(bestMove, nodeEvaluation));
        }
    }
    else {
        if (bestSubtreeRoot) {
            bestSubtreeRoot->setEvaluation(nodeEvaluation);
            parent.setChild(std::move(bestSubtreeRoot));
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
