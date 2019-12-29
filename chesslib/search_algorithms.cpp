#include "search_algorithms.h"

#include "evaluate.h"
#include "Position.h"
#include "require.h"
#include "SearchNode.h"
#include "SearchTree.h"

#include <limits>

namespace chesslib {

double runNegatedMinMax(
    SearchNode& startingNode,
    SearchTree& searchTree,
    const Position& startingPosition)
{
    // TODO: adapt for iterative deepening
    REQUIRE(!startingNode.hasChildren());

    MovesCollection pseudoLegalMoves;
    Position position = startingPosition;
    position.fillWithPseudoLegalMoves(pseudoLegalMoves);

    double bestEvaluation = std::numeric_limits<double>::lowest();
    const double evaluationSideMultiplier = position.getPlayerToMove() == Black ? -1.0 : 1.0;

    for (const Move move : pseudoLegalMoves) {
        position.playMove(move);
        if (position.isValid()) {
            const double evaluation = evaluationSideMultiplier * evaluate(position);
            // TODO: consider not generating SearchNode instances for the deepest level
            if (evaluation > bestEvaluation) {
                searchTree.createBestChildNode(startingNode, move);
                bestEvaluation = evaluation;
            }
            else {
                searchTree.createSuboptimalChildNode(startingNode, move);
            }
        }
        position.undoMove();
    }

    if (!startingNode.hasChildren()) {
        bestEvaluation = evaluateNoLegalMovesPosition(position);
    }

    startingNode.setEvaluation(bestEvaluation);

    return bestEvaluation;
}

}
