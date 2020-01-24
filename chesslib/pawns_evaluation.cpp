#include "pawns_evaluation.h"

#include "Board.h"
#include "squares.h"

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
    getColumnBitboard(COLUMN_A),
    getColumnBitboard(COLUMN_B),
    getColumnBitboard(COLUMN_C),
    getColumnBitboard(COLUMN_D),
    getColumnBitboard(COLUMN_E),
    getColumnBitboard(COLUMN_F),
    getColumnBitboard(COLUMN_G),
    getColumnBitboard(COLUMN_H)
};

// Assumes bitboard != 0
bool isExactlyOneBitSet(const bitboard_t& bitboard)
{
    return (bitboard & (bitboard - 1)) == static_cast<bitboard_t>(0);
}

constexpr bitboard_t getRowBitboard(square_t row)
{
    return static_cast<bitboard_t>(0xFF) << row * 8;
}

constexpr std::array<bitboard_t, PLAYER_COUNT> prePromotionRowsBitboards = {
    getRowBitboard(ROW_2) | getRowBitboard(ROW_3),
    getRowBitboard(ROW_7) | getRowBitboard(ROW_6)
};

class PawnIslandCounter {
public:
    PawnIslandCounter()
    {
        for (uint32_t i = 0; i < islandCount_.size(); ++i) {
            uint32_t islandCount = 0;
            bool isRecentColumnEmpty = true;
            for (uint32_t bit = 1; bit < 256; bit <<= 1) {
                bool isCurrentColumnEmpty = (i & bit) == 0;
                if (!isCurrentColumnEmpty && isRecentColumnEmpty) {
                    ++islandCount;
                }
                isRecentColumnEmpty = isCurrentColumnEmpty;
            }
            islandCount_[i] = islandCount;
        }
    }

    uint32_t getIslandCount(uint32_t pawnsColumnMask) const
    {
        return islandCount_[pawnsColumnMask];
    }

private:
    std::array<uint32_t, 256> islandCount_;
} pawnIslandCounter;

} // namespace

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

void updatePawnFactors(bitboard_t pawnsBitboard, player_t player, PawnEvaluationFactors& pawnFactors)
{
    pawnFactors.pawnsBitboard = pawnsBitboard;

    uint32_t pawnsColumnMask = 0;
    uint32_t columnsWithDoubledPawnsCounter = 0;

    uint32_t columnBit = 1;
    for (const bitboard_t& columnBitboard : columnBitboards) {
        const bitboard_t pawnsColumnBitboard = columnBitboard & pawnsBitboard;
        if (pawnsColumnBitboard) {
            pawnsColumnMask |= columnBit;
            if (!isExactlyOneBitSet(pawnsColumnBitboard)) {
                ++columnsWithDoubledPawnsCounter;
            }
        }
        columnBit <<= 1;
    }

    pawnFactors.pawnsColumnMask = pawnsColumnMask;
    pawnFactors.columnsWithDoubledPawnsCounter = columnsWithDoubledPawnsCounter;

    pawnFactors.hasPawnOnPrePromotionRow =
        ((pawnsBitboard & prePromotionRowsBitboards[player]) == 0ULL) ? 0 : 1;

    pawnFactors.pawnIslandCount = pawnIslandCounter.getIslandCount(pawnFactors.pawnsColumnMask);
}

}
