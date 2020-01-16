#include "MemoryPool.h"

namespace chesslib {

namespace {

constexpr size_t DEFAULT_INITIAL_CAPACITY = 512;

}

MemoryPool::MemoryPool() : MemoryPool(DEFAULT_INITIAL_CAPACITY)
{
}

MemoryPool::MemoryPool(size_t initialCapacity)
{
    movesCollections_.reserve(initialCapacity);
    while (movesCollections_.size() < initialCapacity) {
        movesCollections_.push_back(std::make_unique<MovesCollection>());
    }
}

MemoryPool::PooledPtr MemoryPool::getMovesCollection()
{
    if (movesCollections_.empty()) {
        movesCollections_.push_back(std::make_unique<MovesCollection>());
    }
    auto movesCollection = std::move(movesCollections_.back());
    movesCollections_.pop_back();
    return PooledPtr(*this, std::move(movesCollection));
}

void MemoryPool::releaseMovesCollection(std::unique_ptr<MovesCollection> movesCollection)
{
    if (movesCollection) {
        movesCollections_.push_back(std::move(movesCollection));
    }
}

}
