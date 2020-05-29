namespace ChessEngine.Board
{
    public readonly struct Square
    {
        public readonly int row;
        public readonly int column;

        public Square(int row, int column)
        {
            this.row = row;
            this.column = column;
        }
    }
}
