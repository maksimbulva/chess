using ChessEngine.Board;
using System.Text;

namespace ChessEngine.Bitmask
{
    internal struct Bitmask64
    {
        public long Value { get; private set; }

        public Bitmask64(long value)
        {
            Value = value;
        }

        public void SetBit(int bitIndex)
        {
            Value |= 1L << bitIndex;
        }

        public void UnsetBit(int bitIndex)
        {
            Value &= ~(1L << bitIndex);
        }

        public bool IsBitSet(int bitIndex)
        {
            return (Value & (1L << bitIndex)) != 0L;
        }

        public override string ToString()
        {
            var sb = new StringBuilder();
            for (int i = 0; i < Board.Board.SquareCount; ++i)
            {
                if (IsBitSet(i))
                {
                    if (sb.Length > 0)
                    {
                        sb.Append(',');
                    }
                    sb.Append($"{new BoardSquare(i)}");
                }
            }
            return sb.ToString();
        }
    }
}
