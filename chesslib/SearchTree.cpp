#include "SearchTree.h"

namespace chesslib {

SearchTree::SearchTree(const Position& initialPosition)
    : initialPosition_(initialPosition)
    , root_(Move::NullMove())
{
}

}
