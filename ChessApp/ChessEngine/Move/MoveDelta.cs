using ChessEngine.Board;

namespace ChessEngine.Move
{
    internal readonly struct MoveDelta
    {
        private readonly int deltaRow;
        private readonly int deltaColumn;

        public MoveDelta(int deltaRow, int deltaColumn)
        {
            this.deltaRow = deltaRow;
            this.deltaColumn = deltaColumn;
        }

        public bool IsCanApplyTo(BoardSquare originSquare)
        {
            var destRow = originSquare.Row + deltaRow;
            var destColumn = originSquare.Column + deltaColumn;
            return destRow >= 0 && destRow < Board.Board.RowCount &&
                destColumn >= 0 && destColumn < Board.Board.ColumnCount;
        }

        public BoardSquare GetDestSquare(BoardSquare originSquare)
        {
            return new BoardSquare(
                originSquare.Row + deltaRow,
                originSquare.Column + deltaColumn);
        }
    }
}
