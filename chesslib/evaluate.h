#pragma once

namespace chesslib {

class Position;

double evaluate(const Position& position);

double evaluateNoLegalMovesPosition(Position& position);

}
