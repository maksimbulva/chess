#include "TranspositionTable.h"

namespace chesslib {

TranspositionTable::TranspositionTable()
{
    table_.reserve(MAX_ELEMENT_COUNT);
}

}
