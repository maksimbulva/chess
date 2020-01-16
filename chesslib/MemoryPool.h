#pragma once

#include "MovesCollection.h"

#include <memory>
#include <vector>

namespace chesslib {

class MemoryPool {
public:
    class PooledPtr {
        friend class MemoryPool;
    public:
        PooledPtr(PooledPtr&&) noexcept = default;

        ~PooledPtr()
        {
            memoryPool_->releaseMovesCollection(std::move(movesCollection_));
        }

        MovesCollection* const operator->()
        {
            return &*movesCollection_;
        }

        MovesCollection& operator*()
        {
            return *movesCollection_;
        }

    private:
        PooledPtr(MemoryPool& memoryPool, std::unique_ptr<MovesCollection> movesCollection)
            : memoryPool_(&memoryPool)
            , movesCollection_(std::move(movesCollection))
        {
            movesCollection_->clear();
        }

    private:
        MemoryPool* const memoryPool_;
        std::unique_ptr<MovesCollection> movesCollection_;
    };

public:
    MemoryPool();
    MemoryPool(size_t initialCapacity);

    PooledPtr getMovesCollection();

private:
    friend class PooledPtr;

    void releaseMovesCollection(std::unique_ptr<MovesCollection> movesCollection);

private:
    std::vector<std::unique_ptr<MovesCollection>> movesCollections_;
};

}
