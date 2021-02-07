using ChessEngine.Board;

namespace ChessEngine.Move
{
    internal readonly struct MoveDelta
    {
        public int DeltaRow { get; }
        public int DeltaColumn { get; }

        public MoveDelta(int deltaRow, int deltaColumn)
        {
            DeltaRow = deltaRow;
            DeltaColumn = deltaColumn;
        }

        public MoveDelta(BoardSquare originSquare, BoardSquare destSquare)
        {
            DeltaRow = destSquare.Row - originSquare.Row;
            DeltaColumn = destSquare.Column - originSquare.Column;
        }

        public bool IsCanApplyTo(BoardSquare originSquare)
        {
            var destRow = originSquare.Row + DeltaRow;
            var destColumn = originSquare.Column + DeltaColumn;
            return destRow >= 0 && destRow < Board.Board.RowCount &&
                destColumn >= 0 && destColumn < Board.Board.ColumnCount;
        }

        public BoardSquare GetDestSquare(BoardSquare originSquare)
        {
            return new BoardSquare(
                originSquare.Row + DeltaRow,
                originSquare.Column + DeltaColumn);
        }
    }
}
