#include "string_utils.h"

#include <sstream>

namespace chesslib {

std::vector<std::string> split(const std::string& source, char delimiter)
{
    std::vector<std::string> result;
    std::istringstream stream{ source };
    std::string token;
    while (std::getline(stream, token, delimiter)) {
        if (!token.empty()) {
            result.push_back(token);
        }
    }
    return result;
}

std::string join(std::vector<std::string> strings, char delimiter)
{
    std::string joinedString;
    joinedString.reserve(80);
    for (size_t i = 0; i < strings.size(); ++i) {
        joinedString += strings[i];
        if (i + 1 != strings.size()) {
            joinedString.push_back(delimiter);
        }
    }
    return joinedString;
}

}