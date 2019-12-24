#pragma once

#include <cstdint>
#include <string>

namespace chesslib {

uint64_t countLegalMoves(std::string fenString, int depthPly);

}
