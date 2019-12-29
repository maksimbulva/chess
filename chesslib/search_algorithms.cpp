#include "search_algorithms.h"

#include "evaluate.h"
#include "Position.h"
#include "require.h"
#include "SearchNode.h"
#include "SearchTree.h"

#include <limits>

namespace chesslib {

namespace {

double runRecursiveNegatedMinMax(
    SearchNode& startingNode,
    SearchTree& searchTree,
    Position& position,
    int depthPly)
{
    MovesCollection pseudoLegalMoves;
    position.fillWithPseudoLegalMoves(pseudoLegalMoves);

    double bestEvaluation = std::numeric_limits<double>::lowest();
    Move bestMove = Move::NullMove();

    if (depthPly == 1) {
        const double evaluationSideMultiplier = position.getPlayerToMove() == Black ? -1.0 : 1.0;
        for (const Move move : pseudoLegalMoves) {
            position.playMove(move);
            if (position.isValid()) {
                const double evaluation = evaluationSideMultiplier * evaluate(position);
                if (evaluation > bestEvaluation) {
                    bestEvaluation = evaluation;
                    bestMove = move;
                }
            }
            position.undoMove();
        }
        if (bestMove.isNullMove()) {
            return evaluateNoLegalMovesPosition(position);
        }
        searchTree.createBestChildNode(startingNode, bestMove, bestEvaluation);
    }
    else {
        SearchNodeRef bestSubtreeRoot;
        for (const Move move : pseudoLegalMoves) {
            position.playMove(move);
            if (position.isValid()) {
                SearchNodeRef subtreeRoot = searchTree.createIsolatedNode(move);
                const double evaluation = -runRecursiveNegatedMinMax(
                    *subtreeRoot,
                    searchTree,
                    position,
                    depthPly - 1);
                if (evaluation > bestEvaluation) {
                    bestEvaluation = evaluation;
                    bestSubtreeRoot = std::move(subtreeRoot);
                }
            }
            position.undoMove();
        }
        if (!bestSubtreeRoot) {
            return evaluateNoLegalMovesPosition(position);
        }
        searchTree.insertAsBestChildNode(startingNode, std::move(bestSubtreeRoot));
    }

    return bestEvaluation;
}

}  // namespace

double runNegatedMinMax(
    SearchNode& startingNode,
    SearchTree& searchTree,
    const Position& startingPosition,
    int depthPly)
{
    // TODO: adapt for iterative deepening
    REQUIRE(!startingNode.hasChildren());

    if (depthPly < 1) {
        return 0.0;
    }

    Position position = startingPosition;
    return runRecursiveNegatedMinMax(startingNode, searchTree, position, depthPly);
}

}
