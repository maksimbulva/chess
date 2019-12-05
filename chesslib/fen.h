#pragma once

#include "Position.h"

#include <string>

namespace chesslib {

Position decodeFen(const std::string& fenString);

}