using ChessEngine.Bitmask;

namespace ChessEngine.Board
{
    internal static class BoardBitmasks
    {
        private static readonly Bitmask64[] columnBitmasks = GenerateColumnBitmasks();

        public static Bitmask64 GetRowBitmask(int row) => new Bitmask64(0xFFL << (row * 8));
        public static Bitmask64 GetColumnBitmask(int column) => columnBitmasks[column];

        private static Bitmask64[] GenerateColumnBitmasks()
        {
            Bitmask64[] result = new Bitmask64[Board.ColumnCount];
            for (int i = 0; i < result.Length; ++i)
            {
                result[i] = GenerateColumnBitmask(i);
            }
            return result;
        }

        private static Bitmask64 GenerateColumnBitmask(int column)
        {
            var result = new Bitmask64();
            for (int row = 0; row < Board.RowCount; ++row)
            {
                result.SetBit(new BoardSquare(row, column).IntValue);
            }
            return result;
        }
    }
}
