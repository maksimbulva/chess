namespace ChessEngine.Bitmask
{
    internal struct Bitmask64
    {
        private long _value;

        public Bitmask64(long value)
        {
            _value = value;
        }

        public void SetBit(int bitIndex)
        {
            _value |= 1L << bitIndex;
        }

        public bool IsBitSet(int bitIndex)
        {
            return (_value & (1L << bitIndex)) != 0L;
        }
    }
}
