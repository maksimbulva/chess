namespace ChessEngine.Bitmask
{
    internal struct Bitmask32
    {
        public int Value { get; private set; }

        public Bitmask32(int value)
        {
            Value = value;
        }

        public void SetBit(int bitIndex)
        {
            Value |= 1 << bitIndex;
        }

        public bool IsBitSet(int bitIndex)
        {
            return (Value & (1 << bitIndex)) != 0;
        }
    }
}
