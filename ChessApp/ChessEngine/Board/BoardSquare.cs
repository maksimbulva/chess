namespace ChessEngine.Board
{
    public readonly struct BoardSquare
    {
        private readonly int _intValue;

        public int IntValue => _intValue;

        public int Row => _intValue >> 3;

        public int Column => _intValue & 7;

        public BoardSquare(int value)
        {
            _intValue = value;
        }

        public BoardSquare(int row, int column)
        {
            _intValue = (row << 3) | column;
        }

        public override string ToString()
        {
            return $"{BoardNotation.ColumnToChar(Column)}{BoardNotation.RowToChar(Row)}";
        }
    }
}
