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

    const evaluation_t evaluationSideMultiplier = position.getPlayerToMove() == Black ? -1 : 1;

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
                evaluation = evaluationSideMultiplier * evaluator_->evaluate(position);
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

    if (depthPly == 1) {
        if (!bestMove.isNullMove()) {
            parent.setChild(SearchNode::createRef(bestMove, alpha));
        }
    }
    else {
        if (bestSubtreeRoot) {
            bestSubtreeRoot->setEvaluation(alpha);
            parent.setChild(std::move(bestSubtreeRoot));
        }
    }

    return alpha;
}

}
