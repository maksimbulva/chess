#include "fen.h"

#include "CastleOptions.h"
#include "PieceOnBoard.h"
#include "position_factory.h"
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

player_t decodePlayer(char c)
{
    return std::islower(c) ? Black : White;
}

piece_type_t decodePieceType(char c)
{
    switch (std::tolower(c)) {
    case 'p':
        return Pawn;
    case 'n':
        return Knight;
    case 'b':
        return Bishop;
    case 'r':
        return Rook;
    case 'q':
        return Queen;
    case 'k':
        return King;
    default:
        FAIL();
        return 0;
    }
}

std::vector<PieceOnBoard> decodeBoard(const std::string& boardString)
{
    std::vector<PieceOnBoard> decodedPieces;

    const auto rowStrings = split(boardString, ROWS_SEPARATOR);
    REQUIRE(rowStrings.size() == ROW_COUNT);

    row_t currentRow = ROW_COUNT;
    for (auto& rowString : rowStrings) {
        --currentRow;
        column_t currentColumn = 0;
        for (auto c : rowString) {
            REQUIRE(currentColumn < COLUMN_COUNT);
            if (std::isdigit(c)) {
                currentColumn += c - '0';
            }
            else {
                decodedPieces.push_back({ decodePlayer(c), decodePieceType(c),
                    encodeSquare(currentRow, currentColumn) });
                ++currentColumn;
            }
        }
        REQUIRE(currentColumn == COLUMN_COUNT);
    }
    REQUIRE(currentRow == 0);

    return decodedPieces;
}

player_t decodePlayerToMove(const std::string& encoded)
{
    REQUIRE(encoded.size() == 1);
    switch (std::tolower(encoded.front())) {
    case FEN_BLACK_TO_MOVE:
        return Black;
    case FEN_WHITE_TO_MOVE:
        return White;
    default:
        FAIL();
        return 0;
    }
}

std::array<CastleOptions, PLAYER_COUNT> decodeCastleOptions(const std::string& encoded)
{
    std::array<CastleOptions, PLAYER_COUNT> result;
    
    result[Black].setCanCastleShort(contains(encoded, FEN_BLACK_CAN_CASTLE_SHORT));
    result[Black].setCanCastleLong(contains(encoded, FEN_BLACK_CAN_CASTLE_LONG));
    result[White].setCanCastleShort(contains(encoded, FEN_WHITE_CAN_CASTLE_SHORT));
    result[White].setCanCastleLong(contains(encoded, FEN_WHITE_CAN_CASTLE_LONG));

    return result;
}

position_flags_t decodeEnPassantColumn(const std::string& encoded)
{
    if (encoded.size() > 1) {
        position_flags_t result = encoded.front() - 'a';
        REQUIRE(result >= 0 && result < COLUMN_COUNT);
        return result;
    }
    else {
        return NO_EN_PASSANT_COLUMN;
    }
}

position_flags_t decodeHalfmoveClock(const std::string& encoded)
{
    return std::stoi(encoded);
}

position_flags_t decodeFullmoveNumber(const std::string& encoded)
{
    return std::stoi(encoded);
}

}  // namespace

Position decodeFen(const std::string& fenString)
{
    const auto tokens = split(fenString, ' ');
    REQUIRE(tokens.size() >= 4);

    auto pieces = decodeBoard(tokens[0]);
    player_t playerToMove = decodePlayerToMove(tokens[1]);
    auto castleOptions = decodeCastleOptions(tokens[2]);
    position_flags_t enPassantColumn = decodeEnPassantColumn(tokens[3]);

    position_flags_t halfmoveClock = tokens.size() > 4 ? decodeHalfmoveClock(tokens[4]) : 0;
    position_flags_t fullmoveNumber = tokens.size() > 5 ? decodeFullmoveNumber(tokens[5]) : 0;

    // TODO
    (void)castleOptions;
    (void)enPassantColumn;
    (void)halfmoveClock;
    (void)fullmoveNumber;

    return createPosition(std::move(pieces), playerToMove);
}

}  // namespace chesslib
