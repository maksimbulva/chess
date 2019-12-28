#include "minmax.h"

#include "SearchNode.h"

#include <limits>

namespace chesslib {

const SearchNode* findBestChildNode(const SearchNode& parent, player_t childrenNodesPlayerToMove)
{
    const SearchNode* bestChild = nullptr;
    const SearchNode* currentChild = parent.getChild();
    if (childrenNodesPlayerToMove == Black) {
        // Minimize the score
        double bestEvaluation = std::numeric_limits<double>::max();
        while (currentChild) {
            const double currentChildEvaluation = currentChild->getEvaluation();
            if (currentChild->isEvaluated() && currentChildEvaluation < bestEvaluation) {
                bestEvaluation = currentChildEvaluation;
                bestChild = currentChild;
            }
            currentChild = currentChild->getSibling();
        }
    }
    else {
        // Maximize the score
        double bestEvaluation = std::numeric_limits<double>::min();
        while (currentChild) {
            const double currentChildEvaluation = currentChild->getEvaluation();
            if (currentChild->isEvaluated() && currentChildEvaluation > bestEvaluation) {
                bestEvaluation = currentChildEvaluation;
                bestChild = currentChild;
            }
            currentChild = currentChild->getSibling();
        }
    }

    return bestChild;
}

}
