#include "fen.h"
#include "Position.h"

#include <stdio.h>

using namespace chesslib;

int main()
{
    auto position = decodeFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");

    printf("Hello, world!");
    return 0;
}
