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

    constexpr double alpha = std::numeric_limits<double>::lowest();
    constexpr double beta = std::numeric_limits<double>::max();

    SearchTree searchTree(position);
    const double evaluation = runAlphaBetaSearch(position, searchTree.getRoot(), depthPly, alpha, beta);

    evaluator_ = nullptr;

    return searchTree.getBestVariation();
}

double SearchEngine::runAlphaBetaSearch(
    Position& position,
    SearchNode& parent,
    int depthPly,
    double alpha,
    double beta)
{
    MovesCollection pseudoLegalMoves;
    position.fillWithPseudoLegalMoves(pseudoLegalMoves);

    const double evaluationSideMultiplier = position.getPlayerToMove() == Black ? -1.0 : 1.0;

    Move bestMove = Move::NullMove();
    bool hasLegalMoves = false;

    SearchNodeRef bestSubtreeRoot;
    SearchNodeRef currentSubtreeRoot;

    for (const Move move : pseudoLegalMoves) {
        position.playMove(move);
        if (position.isValid()) {

            hasLegalMoves = true;
 
            double evaluation;
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

            if (evaluation > beta) {
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
        const double evaluation = evaluator_->evaluateNoLegalMovesPosition(position);
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
