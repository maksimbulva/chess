namespace ChessEngine.Hash
{
    internal struct PositionLongHash
    {
        private readonly ulong lowerHash;
        private readonly ulong higherHash;

        public PositionLongHash(uint hash1, uint hash2, uint hash3, uint hash4)
        {
            lowerHash = ((ulong)hash1 << 32) + hash2;
            higherHash = ((ulong)hash3 << 32) + hash4;
        }

        private PositionLongHash(ulong lowerHash, ulong higherHash)
        {
            this.lowerHash = lowerHash;
            this.higherHash = higherHash;
        }

        public PositionLongHash CalculateXor(PositionLongHash other)
        {
            return new PositionLongHash(
                lowerHash ^ other.lowerHash,
                higherHash ^ other.higherHash);
        }
    }
}
