using ChessEngine.Board;
using System;
using System.Collections.Generic;
using System.Text;

namespace ChessEngine.Move
{
    public readonly struct Move
    {
        public static readonly Move NullMove = new Move(0);

        internal const int CaptureFlag = 1 << 21;

        private readonly int packedValue;

        public bool IsNullMove => packedValue == NullMove.packedValue;

        public BoardSquare OriginSquare
        {
            get { return new BoardSquare(packedValue & 0x3F); }
        }

        public BoardSquare DestSquare
        {
            get { return new BoardSquare((packedValue >> 6) & 0x3F); }
        }

        public Piece Piece
        {
            get { return (Piece)((packedValue >> 12) & 7); }
        }

        public bool IsCapture => (packedValue & CaptureFlag) != 0;

        // Use MoveBuilder class to generate packedValue
        internal Move(int packedValue)
        {
            this.packedValue = packedValue;
        }

        public Piece GetCapturedPiece()
        {
            return (Piece)((packedValue >> 18) & 7);
        }

        public override string ToString()
        {
            return $"{Piece} {OriginSquare}-{DestSquare}";
        }
    }
}
