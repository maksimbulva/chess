#include "Variation.h"

#include "SearchNode.h"

namespace chesslib {

Variation::Variation(const SearchNode* startingNode)
    : evaluation_(startingNode->getEvaluation())
{
    // TODO: Search tree must be locked at the moment
    moves_.reserve(32);

    const SearchNode* currentNode = startingNode;
    while (currentNode != nullptr) {
        moves_.push_back(currentNode->getMove());
        currentNode = currentNode->getBestChild();
    }
}

}
