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

char encodePlayerPieceType(player_t player, piece_type_t pieceType)
{
    char pieceChar;
    switch (pieceType)
    {
    case Pawn:
        pieceChar = 'p';
        break;
    case Knight:
        pieceChar = 'n';
        break;
    case Bishop:
        pieceChar = 'b';
        break;
    case Rook:
        pieceChar = 'r';
        break;
    case Queen:
        pieceChar = 'q';
        break;
    case King:
        pieceChar = 'k';
        break;
    default:
        FAIL();
        pieceChar = '\0';
    }
    return player == Black ? pieceChar : std::toupper(pieceChar);
}

std::vector<PieceOnBoard> decodeBoard(const std::string& boardString)
{
    std::vector<PieceOnBoard> decodedPieces;

    const auto rowStrings = split(boardString, ROWS_SEPARATOR);
    REQUIRE(rowStrings.size() == ROW_COUNT);

    square_t currentRow = ROW_COUNT;
    for (auto& rowString : rowStrings) {
        --currentRow;
        square_t currentColumn = 0;
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

std::string encodeBoard(const Board& board)
{
    std::string encodedBoard;

    for (square_t currentRow = MAX_ROW; currentRow >= 0; --currentRow) {
        std::string encodedRow;
        fastint emptySquaresCounter = 0;
        for (square_t currentColumn = 0; currentColumn < COLUMN_COUNT; ++currentColumn) {
            const square_t currentSquare = encodeSquare(currentRow, currentColumn);
            if (board.isEmpty(currentSquare)) {
                ++emptySquaresCounter;
            }
            else {
                if (emptySquaresCounter != 0) {
                    encodedRow += std::to_string(emptySquaresCounter);
                    emptySquaresCounter = 0;
                }
                encodedRow.push_back(encodePlayerPieceType(
                    board.getPlayer(currentSquare),
                    board.getPieceTypeAt(currentSquare)));
            }
        }
        if (emptySquaresCounter != 0) {
            encodedRow += std::to_string(emptySquaresCounter);
        }
        encodedBoard += encodedRow;
        if (currentRow != 0) {
            encodedBoard.push_back(ROWS_SEPARATOR);
        }
    }

    return encodedBoard;
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

std::string encodePlayerToMove(player_t playerToMove)
{
    return { playerToMove == Black ? FEN_BLACK_TO_MOVE : FEN_WHITE_TO_MOVE };
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

std::string encodeCastleOptions(const std::array<CastleOptions, PLAYER_COUNT>& castleOptions)
{
    std::string encodedCastleOptions;

    if (castleOptions[White].isCanCastleShort()) {
        encodedCastleOptions.push_back(FEN_WHITE_CAN_CASTLE_SHORT);
    }
    if (castleOptions[White].isCanCastleLong()) {
        encodedCastleOptions.push_back(FEN_WHITE_CAN_CASTLE_LONG);
    }
    if (castleOptions[Black].isCanCastleShort()) {
        encodedCastleOptions.push_back(FEN_BLACK_CAN_CASTLE_SHORT);
    }
    if (castleOptions[Black].isCanCastleLong()) {
        encodedCastleOptions.push_back(FEN_BLACK_CAN_CASTLE_LONG);
    }
    if (encodedCastleOptions.empty()) {
        encodedCastleOptions = FEN_NO_ONE_CAN_CASTLE;
    }

    return encodedCastleOptions;
}

OptionalColumn decodeEnPassantColumn(const std::string& encoded)
{
    if (encoded.size() > 1) {
        const square_t result = encoded.front() - 'a';
        REQUIRE(result >= 0 && result < COLUMN_COUNT);
        return OptionalColumn::fromColumn(result);
    }
    else {
        return OptionalColumn();
    }
}

std::string encodeEnPassantColumn(player_t playerToMove, OptionalColumn enPassantColumn)
{
    std::string encodedEnPassantColumn;

    if (enPassantColumn.hasValue()) {
        encodedEnPassantColumn.push_back('a' + enPassantColumn.getColumn());
        encodedEnPassantColumn.push_back(playerToMove == Black ? '3' : '6');
    }
    else {
        encodedEnPassantColumn = FEN_CANNOT_CAPTURE_EN_PASSANT;
    }

    return encodedEnPassantColumn;
}

uint32_t decodeHalfmoveClock(const std::string& encoded)
{
    return static_cast<uint32_t>(std::stoi(encoded));
}

std::string encodeHalfmoveClock()
{
    // TODO: Return actual value
    return std::to_string(0);
}

uint32_t decodeFullmoveNumber(const std::string& encoded)
{
    return static_cast<uint32_t>(std::stoi(encoded));
}

std::string encodeFullmoveNumber()
{
    // TODO: Return actual value
    return std::to_string(1);
}

}  // namespace

Position decodeFen(const std::string& fenString)
{
    const auto tokens = split(fenString, ' ');
    REQUIRE(tokens.size() >= 4);

    auto pieces = decodeBoard(tokens[0]);
    const player_t playerToMove = decodePlayerToMove(tokens[1]);
    auto castleOptions = decodeCastleOptions(tokens[2]);
    const auto enPassantColumn = decodeEnPassantColumn(tokens[3]);

    auto halfmoveClock = tokens.size() > 4 ? decodeHalfmoveClock(tokens[4]) : 0;
    auto fullmoveNumber = tokens.size() > 5 ? decodeFullmoveNumber(tokens[5]) : 0;

    return createPosition(
        std::move(pieces),
        playerToMove,
        enPassantColumn,
        castleOptions[White],
        castleOptions[Black],
        halfmoveClock,
        fullmoveNumber);
}

std::string encodeFen(const Position& position)
{
    return join({
            encodeBoard(position.getBoard()),
            encodePlayerToMove(position.getPlayerToMove()),
            encodeCastleOptions({ position.getCastleOptions(Black), position.getCastleOptions(White) }),
            encodeEnPassantColumn(position.getPlayerToMove(), position.getEnPassantColumn()),
            encodeHalfmoveClock(),
            encodeFullmoveNumber()
        },
        ' ');
}

}  // namespace chesslib
