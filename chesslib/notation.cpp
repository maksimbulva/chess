#include "notation.h"

#include "require.h"
#include "string_repr.h"

namespace chesslib {

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
    // TODO: promotions
    REQUIRE(moveString.size() == 4);
    const auto originSquare = parseSquare(moveString.substr(0, 2));
    const auto destSquare = parseSquare(moveString.substr(2, 2));
    return { originSquare, destSquare };
}

std::string toCoordinateNotation(Move move)
{
    // TODO: promotions
    REQUIRE(!move.isPromotion());
    std::string result = squareToString(move.getOriginSquare());
    result += squareToString(move.getDestSquare());
    return result;
}

}