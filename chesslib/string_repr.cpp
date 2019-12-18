#include "string_repr.h"

namespace chesslib {

namespace {

char rowToChar(square_t row)
{
    return '1' + row;
}

char columnToChar(square_t column)
{
    return 'a' + column;
}

char pieceTypeToChar(piece_type_t pieceType)
{
    switch (pieceType)
    {
    case Knight:
        return 'N';
    case Bishop:
        return 'B';
    case Rook:
        return 'R';
    case Queen:
        return 'Q';
    case King:
        return 'K';
    default:
        return '?';
    }
}

}

std::string squareToString(square_t square)
{
    std::string result;
    result.push_back(columnToChar(getColumn(square)));
    result.push_back(rowToChar(getRow(square)));
    return result;
}

std::string moveToString(Move move)
{
    // TODO: make it proper for all cases
    std::string result;
    result.push_back(pieceTypeToChar(move.getPieceType()));
    result += squareToString(move.getOriginSquare());
    result += move.isCapture() ? ':' : '-';
    result += squareToString(move.getDestSquare());
    return result;
}

std::string historyToString(const PositionHistory& history)
{
    // TODO: include position flags as well
    std::string result = moveToString(history.getMove());
    const OptionalColumn enPassantColumn = history.getPositionFlags().getEnPassantColumn();
    result.append("\tep=");
    result.append(std::string{enPassantColumn.hasValue() ? columnToChar(enPassantColumn.getColumn()) : '-'});
    return result;
}

std::string historyCollectionToString(const std::vector<PositionHistory>& history)
{
    std::string result;
    for (const auto& item : history) {
        result.append(historyToString(item));
        result.append("\n");
    }
    return result;
}

}