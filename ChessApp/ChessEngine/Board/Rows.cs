namespace ChessEngine.Board
{
    public static class Rows
    {
        public const int BlackInitialRow = 7;
        public const int WhiteInitialRow = 0;

        public static int GetInitialRow(Player player)
        {
            return player == Player.Black ? BlackInitialRow : WhiteInitialRow;
        }
    }
}
