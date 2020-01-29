#pragma once

#include <random>

namespace chesslib {

class Random
{
public:
    Random()
        : generator_(rndDevice_())
    {
    }

    uint64_t getNextU64()
    {
        return distribution_(generator_);
    }

private:
    std::random_device rndDevice_;
    std::mt19937_64 generator_;
    std::uniform_int_distribution<uint64_t> distribution_;
};

}
