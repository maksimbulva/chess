#include "MemoryPool.h"

namespace chesslib {

namespace {

constexpr size_t POOL_SIZE = 512;

}

MemoryPool::MemoryPool()
{
    movesCollections_.resize(POOL_SIZE);
}

MemoryPool::PooledPtr MemoryPool::getMovesCollection()
{
    if (movesCollections_.empty()) {
        movesCollections_.push_back(MovesCollection());
    }
    auto movesCollection = std::move(movesCollections_.back());
    movesCollections_.pop_back();
    return PooledPtr(*this, std::move(movesCollection));
}

void MemoryPool::releaseMovesCollection(MovesCollection movesCollection)
{
    movesCollections_.push_back(std::move(movesCollection));
}

}
