﻿using ChessEngine.Board;

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

        public MoveBuilder SetCapture(Piece capturedPiece)
        {
            return new MoveBuilder(packedValue | Move.CaptureFlag | ((int)capturedPiece << 18));
        }

        public MoveBuilder SetEnPassantCapture()
        {
            return new MoveBuilder(packedValue | Move.CaptureFlag | Move.EnPassantCapture |
                ((int)Piece.Pawn << 18));
        }

        public MoveBuilder SetPromotion(Piece promoteToPiece)
        {
            return new MoveBuilder(packedValue | Move.Promotion | ((int)promoteToPiece << 15));
        }

        public MoveBuilder SetShortCastle()
        {
            return new MoveBuilder(packedValue | Move.ShortCastle);
        }

        public MoveBuilder SetLongCastle()
        {
            return new MoveBuilder(packedValue | Move.LongCastle);
        }

        public MoveBuilder SetPawnDoubleMove()
        {
            return new MoveBuilder(packedValue | Move.PawnDoubleMove);
        }
    }
}
