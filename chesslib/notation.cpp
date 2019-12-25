#include "notation.h"

#include "require.h"
#include "string_repr.h"

namespace chesslib {

namespace {

constexpr std::size_t MOVE_WITHOUT_CAPTURE_STRING_LENGTH = 4;
constexpr std::size_t MOVE_WITH_CAPTURE_STRING_LENGTH = 5;

piece_type_t parsePieceType(char pieceTypeChar)
{
    switch (std::tolower(pieceTypeChar)) {
    case 'n':
        return Knight;
    case 'b':
        return Bishop;
    case 'r':
        return Rook;
    case 'q':
        return Queen;
    default:
        FAIL();
        return NoPiece;
    }
}

char pieceTypeToChar(piece_type_t pieceType)
{
    switch (pieceType) {
    case Knight:
        return 'n';
    case Bishop:
        return 'b';
    case Rook:
        return 'r';
    case Queen:
        return 'q';
    default:
        FAIL();
        return '?';
    }
}

}

square_t parseSquare(std::string squareString)
{
    REQUIRE(squareString.size() == 2);
    char columnChar = std::tolower(squareString[0]);
    char rowChar = squareString[1];
    REQUIRE(columnChar >= 'a' && columnChar <= 'h' && rowChar >= '1' && rowChar <= '8');
    return encodeSquare(rowChar - '1', columnChar - 'a');
}

ParsedMove parseCoordinateNotation(std::string moveString)
{
    const auto originSquare = parseSquare(moveString.substr(0, 2));
    const auto destSquare = parseSquare(moveString.substr(2, 2));
    if (moveString.size() == MOVE_WITHOUT_CAPTURE_STRING_LENGTH) {
        return { originSquare, destSquare, NoPiece };
    }
    else if (moveString.size() == MOVE_WITH_CAPTURE_STRING_LENGTH) {
        const auto promoteToPieceType = parsePieceType(moveString[4]);
        return { originSquare, destSquare, promoteToPieceType };
    }
    else {
        FAIL();
        return { 0, 0, NoPiece };
    }
}

std::string toCoordinateNotation(Move move)
{
    std::string result = squareToString(move.getOriginSquare());
    result += squareToString(move.getDestSquare());
    if (move.isPromotion()) {
        result.push_back(pieceTypeToChar(move.getPromoteToPieceType()));
    }
    return result;
}

}