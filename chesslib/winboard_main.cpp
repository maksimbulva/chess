#include "fen.h"
#include "Position.h"

#include <iostream>

using namespace chesslib;

int main()
{
    auto position = decodeFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");

    MovesCollection moves;
    position.fillWithLegalMoves(moves);

    std::cout << "Hello, world!" << std::endl;
    std::cout << moves.size() << std::endl;

    return 0;
}
