#include "SearchEngine.h"

#include "Evaluator.h"
#include "MovesCollection.h"
#include "require.h"
#include "SearchTree.h"

#include <limits>

namespace chesslib {

Variation SearchEngine::runSearch(Position position, Evaluator& evaluator, int depthPly)
{
    REQUIRE(depthPly > 0);

    evaluator_ = &evaluator;

    constexpr evaluation_t beta = std::numeric_limits<evaluation_t>::max();
    constexpr evaluation_t alpha = -beta;

    SearchTree searchTree(position);
    const evaluation_t evaluation = runAlphaBetaSearch(position, searchTree.getRoot(), depthPly, alpha, beta);

    evaluator_ = nullptr;

    return searchTree.getBestVariation();
}

evaluation_t SearchEngine::runAlphaBetaSearch(
    Position& position,
    SearchNode& parent,
    int depthPly,
    evaluation_t alpha,
    evaluation_t beta)
{
    MovesCollection pseudoLegalMoves;
    position.fillWithPseudoLegalMoves(pseudoLegalMoves);

    const evaluation_t evaluationSideMultiplier = Evaluator::getSideMultiplier(position.getPlayerToMove());

    Move bestMove = Move::NullMove();
    bool hasLegalMoves = false;

    SearchNodeRef bestSubtreeRoot;
    SearchNodeRef currentSubtreeRoot;

    for (const Move move : pseudoLegalMoves) {
        position.playMove(move);
        if (position.isValid()) {

            hasLegalMoves = true;
 
            evaluation_t evaluation;
            if (depthPly == 1) {
                if (move.isCapture() || move.isPromotion()) {
                    evaluation = -runQuiescentSearch(position, -beta, -alpha);
                }
                else {
                    evaluation = evaluationSideMultiplier * evaluator_->evaluate(position);
                }
            }
            else {
                currentSubtreeRoot = SearchNode::createRef(move);
                evaluation = -runAlphaBetaSearch(
                    position,
                    *currentSubtreeRoot,
                    depthPly - 1,
                    -beta,
                    -alpha);
            }

            if (evaluation >= beta) {
                position.undoMove();
                return beta;
            } 
            
            if (evaluation > alpha) {
                alpha = evaluation;
                bestMove = move;
                bestSubtreeRoot = std::move(currentSubtreeRoot);
            }
        }
        position.undoMove();
    }

    if (!hasLegalMoves) {
        const evaluation_t evaluation = evaluator_->evaluateNoLegalMovesPosition(position);
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
    Position& position,
    evaluation_t alpha,
    evaluation_t beta)
{
    const evaluation_t evaluationSideMultiplier = Evaluator::getSideMultiplier(position.getPlayerToMove());
    const evaluation_t evaluation = evaluationSideMultiplier * evaluator_->evaluate(position);

    if (evaluation >= beta) {
        return beta;
    }

    if (evaluation > alpha) {
        alpha = evaluation;
    }

    MovesCollection pseudoLegalMoves;
    // TODO: optimize by looking for captures only
    position.fillWithPseudoLegalMoves(pseudoLegalMoves);

    for (const Move move : pseudoLegalMoves) {
        if (!move.isCapture()) {
            continue;
        }

        position.playMove(move);
        if (position.isValid()) {
            evaluation_t subtreeEvaluation = -runQuiescentSearch(position, -beta, -alpha);

            if (subtreeEvaluation >= beta) {
                position.undoMove();
                return beta;
            }

            if (subtreeEvaluation > alpha) {
                alpha = subtreeEvaluation;
            }
        }
        position.undoMove();
    }

    return alpha;
}

}
