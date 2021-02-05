namespace ChessEngine.Internal
{
    internal static class Utils
    {
        public static Player GetOtherPlayer(Player player)
        {
            return player == Player.Black ? Player.White : Player.Black;
        }
    }
}
