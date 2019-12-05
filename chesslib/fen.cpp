#include "fen.h"
#include "require.h"
#include "string_utils.h"

#include <cctype>

namespace {
    constexpr char ROWS_SEPARATOR = '/';
    constexpr char FEN_WHITE_TO_MOVE = 'w';
    constexpr char FEN_BLACK_TO_MOVE = 'b';
    constexpr char FEN_NO_ONE_CAN_CASTLE = '-';
    constexpr char FEN_WHITE_CAN_CASTLE_SHORT = 'K';
    constexpr char FEN_WHITE_CAN_CASTLE_LONG = 'Q';
    constexpr char FEN_BLACK_CAN_CASTLE_SHORT = 'k';
    constexpr char FEN_BLACK_CAN_CASTLE_LONG = 'q';
    constexpr char FEN_CANNOT_CAPTURE_EN_PASSANT = '-';
}

namespace chesslib {

namespace {

void decodeBoard(const std::string& boardString)
{
    const auto rowStrings = split(boardString, ROWS_SEPARATOR);
    REQUIRE(rowStrings.size() != ROW_COUNT);

    row_t currentRow = ROW_COUNT;
    for (auto& rowString : rowStrings) {
        --currentRow;
        column_t currentColumn = 0;
        for (auto c : rowString) {
            REQUIRE(currentColumn < COLUMN_COUNT);
        }
    }
}

}

Position decodeFen(const std::string& fenString)
{
    const auto tokens = split(fenString, ' ');

    throw std::exception{ };
}

}