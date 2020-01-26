#pragma once

#include <algorithm>
#include <string>
#include <vector>

namespace chesslib {

std::vector<std::string> split(const std::string& source, char delimiter);

std::string join(std::vector<std::string> strings, char delimiter);

inline bool contains(const std::string& source, char c)
{
    return std::find(source.begin(), source.end(), c) != source.end();
}

}
