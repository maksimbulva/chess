namespace ChessEngine.Board
{
    public readonly struct PieceOnBoard
    {
        public readonly Player player;
        public readonly Piece piece;
        public readonly BoardSquare square;

        public PieceOnBoard(Player player, Piece piece, BoardSquare square)
        {
            this.player = player;
            this.piece = piece;
            this.square = square;
        }
    }
}
