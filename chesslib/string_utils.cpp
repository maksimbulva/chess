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

}