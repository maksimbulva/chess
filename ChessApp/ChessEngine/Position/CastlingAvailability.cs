namespace ChessEngine.Position
{
    public readonly struct CastlingAvailability
    {
        public static readonly CastlingAvailability None = new CastlingAvailability(false, false);

        public bool CanCastleShort { get; }
        public bool CanCastleLong { get; }

        public CastlingAvailability(bool canCastleShort, bool canCastleLong)
        {
            CanCastleShort = canCastleShort;
            CanCastleLong = canCastleLong;
        }
    }
}
