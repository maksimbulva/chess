namespace ChessEngine
{
    struct ColoredPiece
    {
        public readonly Player player;
        public readonly Piece piece;

        public ColoredPiece(Player player, Piece piece)
        {
            this.player = player;
            this.piece = piece;
        }
    }
}
