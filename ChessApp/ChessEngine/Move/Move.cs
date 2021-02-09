using ChessEngine.Board;
using static ChessEngine.Board.Columns;
using static ChessEngine.Board.Rows;

namespace ChessEngine.Move
{
    public readonly struct Move
    {
        public static readonly Move NullMove = new Move(0);

        private static readonly Move[] shortCastleRookMoves = GenerateShortCastleRookMoves();
        private static readonly Move[] longCastleRookMoves = GenerateLongCastleRookMoves();

        internal const int CaptureFlag = 1 << 21;
        internal const int EnPassantCapture = 1 << 22;
        internal const int Promotion = 1 << 23;
        internal const int ShortCastle = 1 << 24;
        internal const int LongCastle = 1 << 25;
        internal const int PawnDoubleMove = 1 << 26;

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

        public bool IsEnPassantCapture => (packedValue & EnPassantCapture) != 0;

        public bool IsPromotion => (packedValue & Promotion) != 0;

        public bool IsShortCastle => (packedValue & ShortCastle) != 0;

        public bool IsLongCastle => (packedValue & LongCastle) != 0;

        public bool IsPawnDoubleMove => (packedValue & PawnDoubleMove) != 0;

        // Use MoveBuilder class to generate packedValue
        internal Move(int packedValue)
        {
            this.packedValue = packedValue;
        }

        public Piece GetPromoteToPiece()
        {
            return (Piece)((packedValue >> 15) & 7);
        }

        public Piece GetCapturedPiece()
        {
            return (Piece)((packedValue >> 18) & 7);
        }

        public override string ToString()
        {
            return $"{Piece} {OriginSquare}-{DestSquare}";
        }

        internal static Move GetShortCastleRookMove(Player player) => shortCastleRookMoves[(int)player];

        internal static Move GetLongCastleRookMove(Player player) => longCastleRookMoves[(int)player];

        private static Move[] GenerateShortCastleRookMoves()
        {
            var result = new Move[2];
            result[(int)Player.Black] = GenerateRookCastleMove(Player.Black, ColumnH, ColumnF);
            result[(int)Player.White] = GenerateRookCastleMove(Player.White, ColumnH, ColumnF);
            return result;
        }

        private static Move[] GenerateLongCastleRookMoves()
        {
            var result = new Move[2];
            result[(int)Player.Black] = GenerateRookCastleMove(Player.Black, ColumnA, ColumnD);
            result[(int)Player.White] = GenerateRookCastleMove(Player.White, ColumnA, ColumnD);
            return result;
        }

        private static Move GenerateRookCastleMove(Player player, int originColumn, int destColumn)
        {
            return new MoveBuilder(Piece.Rook, new BoardSquare(GetInitialRow(player), originColumn))
                .SetDestSquare(new BoardSquare(GetInitialRow(player), destColumn))
                .Build();
        }
    }
}
