#pragma once

#include "MovesCollection.h"

#include <vector>

namespace chesslib {

class MemoryPool {
public:
    class PooledPtr {
        friend class MemoryPool;
    public:
        ~PooledPtr()
        {
            memoryPool_->releaseMovesCollection(std::move(movesCollection_));
        }

        MovesCollection* const operator->()
        {
            return &movesCollection_;
        }

        MovesCollection& operator*()
        {
            return movesCollection_;
        }

    private:
        PooledPtr(MemoryPool& memoryPool, MovesCollection movesCollection)
            : memoryPool_(&memoryPool)
            , movesCollection_(std::move(movesCollection))
        {
            movesCollection_.clear();
        }

    private:
        MemoryPool* const memoryPool_;
        MovesCollection movesCollection_;
    };

public:
    MemoryPool();

    PooledPtr getMovesCollection();

private:
    friend class PooledPtr;

    void releaseMovesCollection(MovesCollection movesCollection);

private:
    std::vector<MovesCollection> movesCollections_;
};

}
