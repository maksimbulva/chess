using ChessEngine.Board;
using System;
using System.Collections.Generic;
using System.Text;

namespace ChessEngine.Move
{
    public readonly struct Move
    {
        private readonly int packedValue;

        public static readonly Move NullMove = new Move(0);

        public bool IsNullMove => packedValue == NullMove.packedValue;

        public BoardSquare OriginSquare
        {
            get { return new BoardSquare(packedValue & 0x3F); }
        }

        public BoardSquare DestSquare
        {
            get { return new BoardSquare((packedValue >> 6) & 0x3F); }
        }

        // Use MoveBuilder class to generate packedValue
        internal Move(int packedValue)
        {
            this.packedValue = packedValue;
        }
    }
}
