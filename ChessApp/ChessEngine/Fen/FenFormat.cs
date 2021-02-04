using System;
using System.Collections.Generic;

namespace ChessEngine.Fen
{
    internal static class FenFormat
    {
        public const char RowSeparator = '/';
        public const string BlackToMove = "b";
        public const string WhiteToMove = "w";
        public const string CanCastleShort = "k";
        public const string CanCastleLong = "q";
        public const string NoOneCanCastle = "-";
        public const string CannotCaptureEnPassant = "-";

        public const string WhiteEnPassantCaptureRow = "6";
        public const string BlackEnPassantCaptureRow = "3";

        private static readonly List<Tuple<Piece, char>> _pieceEncoding = new List<Tuple<Piece, char>>()
        {
            new Tuple<Piece, char>(Piece.Pawn, 'p'),
            new Tuple<Piece, char>(Piece.Knight, 'n'),
            new Tuple<Piece, char>(Piece.Bishop, 'b'),
            new Tuple<Piece, char>(Piece.Rook, 'r'),
            new Tuple<Piece, char>(Piece.Queen, 'q'),
            new Tuple<Piece, char>(Piece.King, 'k')
        };

        public static Piece DecodePiece(char encodedPiece)
        {
            return _pieceEncoding.Find(x => x.Item2 == encodedPiece).Item1;
        }
    }
}
