#include "string_repr.h"

#include "Board.h"

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

std::string castleOptionsToString(CastleOptions castleOptions)
{
    std::string result;
    if (castleOptions.isCannotCastle()) {
        return "No";
    }
    else if (castleOptions.isCanCastleLong() && castleOptions.isCanCastleShort()) {
        return "Both";
    }
    else if (castleOptions.isCanCastleLong()) {
        return "Only long";
    }
    else if (castleOptions.isCanCastleShort()) {
        return "Only short";
    }
    else {
        return "ERROR";
    }
}

}  // namespace

std::string playerToString(player_t player)
{
    if (player == Black) {
        return "Black";
    }
    else {
        return "White";
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
    if (move.isLongCastle()) {
        return "O-O-O";
    }
    else if (move.isShortCastle()) {
        return "O-O";
    }
    else {
        // TODO: make it proper for all cases
        std::string result;
        result.push_back(pieceTypeToChar(move.getPieceType()));
        result += squareToString(move.getOriginSquare());
        result += move.isCapture() ? ':' : '-';
        result += squareToString(move.getDestSquare());
        return result;
    }
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

std::string boardToString(const Board& board)
{
    std::string result;
    result.reserve(80);

    for (square_t row = MAX_ROW; row >= 0; --row) {
        for (square_t column = 0; column < COLUMN_COUNT; ++column) {
            const square_t currentSquare = encodeSquare(row, column);
            if (board.isEmpty(currentSquare)) {
                result.push_back('.');
            }
            else {
                char c = pieceTypeToChar(board.getPieceTypeAt(currentSquare));
                if (board.getPlayer(currentSquare) == Black) {
                    c = std::tolower(c);
                }
                result.push_back(c);
            }
        }
        result.push_back('\n');
    }

    return result;
}

std::string positionFlagsToString(PositionFlags flags)
{
    std::string result;
    result.append("Player to move:\t");
    result.append(playerToString(flags.getPlayerToMove()));
    result.append("\nWhite castle options:\t");
    result.append(castleOptionsToString(flags.getCastleOptions(White)));
    result.append("\nBlack castle options:\t");
    result.append(castleOptionsToString(flags.getCastleOptions(Black)));
    return result;
}

}