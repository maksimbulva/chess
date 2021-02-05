using ChessEngine.Board;

namespace ChessEngine.Move
{
    // Data are packed in a 32-bit integer for sake of memory efficiency
    internal readonly struct MoveBuilder
    {
        private readonly int packedValue;

        public MoveBuilder(Piece pieceToMove, BoardSquare originSquare)
        {
            packedValue = originSquare.IntValue;
            packedValue |= (int)pieceToMove << 12;
        }

        private MoveBuilder(int packedValue)
        {
            this.packedValue = packedValue;
        }

        public Move Build() => new Move(packedValue);

        public MoveBuilder SetDestSquare(BoardSquare destSquare)
        {
            return new MoveBuilder(packedValue | (destSquare.IntValue << 6));
        }
    }
}
