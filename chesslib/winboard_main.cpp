#include "fen.h"
#include "Position.h"

#include <cstdint>
#include <iostream>

using namespace chesslib;

uint64_t countMoves(Position& position, uint32_t depth)
{
    if (depth == 0) {
        return 1;
    }

    uint64_t result = 0;

    MovesCollection moves;
    position.fillWithPseudoLegalMoves(moves);

    for (const Move& move : moves) {
        position.playMove(move);
        if (position.isKingCanBeCaptured()) {
            continue;
        }
        if (depth == 1) {
            ++result;
        }
        else {
            result += countMoves(position, depth - 1);
        }
        position.undoMove();
    }
    return result;
}

int main()
{
    std::cout << "Perft results:" << std::endl;

    for (uint32_t depth = 1; depth <= 2; ++depth) {
        auto position = decodeFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        uint64_t moveCount = countMoves(position, depth);
        std::cout << "\tDepth: " << depth << "\tMoves: " << moveCount << std::endl;
    }

    return 0;
}
