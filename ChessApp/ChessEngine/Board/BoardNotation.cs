using System;

namespace ChessEngine.Board
{
    public static class BoardNotation
    {
        public static char RowToChar(int row)
        {
            return (char)('1' + row);
        }

        public static char ColumnToChar(int column)
        {
            return (char)('a' + column);
        }

        public static int CharToRow(char rowChar)
        {
            if (rowChar < '1' || rowChar > '8')
            {
                throw new ArgumentException($"Cannot convert {rowChar} to board row");
            }
            return rowChar - '1';
        }

        public static int CharToColumn(char columnChar)
        {
            columnChar = Char.ToLower(columnChar);
            if (columnChar < 'a' || columnChar > 'h')
            {
                throw new ArgumentException($"Cannot convert {columnChar} to board column");
            }
            return columnChar - 'a';
        }

        public static BoardSquare SquareFromString(string coordinatesStr)
        {
            if (coordinatesStr.Length != 2)
            {
                throw new ArgumentException($"Cannot parse board square from {coordinatesStr}");
            }
            return new BoardSquare(
                row: CharToRow(coordinatesStr[1]),
                column: CharToColumn(coordinatesStr[0]));
        }
    }
}
