#pragma once

#include <algorithm>
#include <chrono>
#include <cstdint>

namespace chesslib {

class Stopwatch {
public:
    Stopwatch()
        : start_(std::chrono::steady_clock::now())
    {
    }

    int64_t getElapsedMilliseconds() const
    {
        auto elapsed = std::chrono::duration_cast<std::chrono::milliseconds>(
            std::chrono::steady_clock::now() - start_).count();
        return static_cast<int64_t>(std::max<decltype(elapsed)>(0, elapsed));
    }

private:
    std::chrono::time_point<std::chrono::steady_clock> start_;
};

}