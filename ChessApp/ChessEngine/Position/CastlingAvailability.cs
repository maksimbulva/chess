using ChessEngine.Bitmask;

namespace ChessEngine.Position
{
    public readonly struct CastlingAvailability
    {
        public static readonly CastlingAvailability None = new CastlingAvailability(false, false);

        private const int BitCanCastleShort = 0;
        private const int BitCanCastleLong = 1;

        private readonly Bitmask32 flags;

        public bool CanCastle => flags.Value == 0;
        public bool CanCastleShort => flags.IsBitSet(BitCanCastleShort);
        public bool CanCastleLong => flags.IsBitSet(BitCanCastleLong);

        public CastlingAvailability(bool canCastleShort, bool canCastleLong)
        {
            flags = new Bitmask32();
            if (canCastleShort)
            {
                flags.SetBit(BitCanCastleShort);
            }
            if (canCastleLong)
            {
                flags.SetBit(BitCanCastleLong);
            }
        }
    }
}
