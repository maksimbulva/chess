namespace ChessEngine
{
    public static class ChessEngineFactory
    {
        public static IChessEngine CreateChessEngine() => new Internal.ChessEngine();
    }
}
