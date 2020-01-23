#include "pawns_evaluation.h"

#include "Board.h"

#include <array>

namespace chesslib {

namespace {

constexpr bitboard_t getColumnBitboard(square_t column)
{
    bitboard_t bb = 0;
    for (square_t row = 0; row < ROW_COUNT; ++row) {
        setSquare(bb, encodeSquare(row, column));
    }
    return bb;
}

constexpr std::array<bitboard_t, COLUMN_COUNT> columnBitboards = {
    getColumnBitboard(0),
    getColumnBitboard(1),
    getColumnBitboard(2),
    getColumnBitboard(3),
    getColumnBitboard(4),
    getColumnBitboard(5),
    getColumnBitboard(6),
    getColumnBitboard(7)
};

}

bitboard_t getPawnsBitboard(const Board& board, player_t player)
{
    uint64_t pawnsBitboard = 0;
    board.doForEachPiece(player, [&pawnsBitboard] (piece_type_t pieceType, square_t square)
        {
            if (pieceType == Pawn) {
                setSquare(pawnsBitboard, square);
            }
        });
    return pawnsBitboard;
}

uint32_t getPawnsColumnMask(bitboard_t pawnsBitboard)
{
    uint32_t mask = 0;
    uint32_t columnBit = 1;
    for (const bitboard_t& columnBitboard : columnBitboards) {
        if (columnBitboard & pawnsBitboard) {
            mask |= columnBit;
        }
        columnBit <<= 1;
    }
    return mask;
}

}
