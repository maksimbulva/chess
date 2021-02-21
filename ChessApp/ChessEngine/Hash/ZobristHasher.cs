using ChessEngine.Board;
using ChessEngine.Common;
using ChessEngine.Position;
using Optional;
using System.Collections.Generic;

namespace ChessEngine.Hash
{
    internal sealed class ZobristHasher
    {
        private static readonly int PieceTypeCount = typeof(Piece).GetEnumValues().Length;
        private static readonly int PlayerCount = typeof(Player).GetEnumValues().Length;

        private const int RndSeed = 1234567;

        private readonly PositionLongHash[] pieceHashesTable;
        private readonly PositionLongHash[] playerToMoveHashesTable;
        private readonly PositionLongHash[] enPassantCaptureColumnHashesTable;
        private readonly PositionLongHash[] shortCastlingHashesTable;
        private readonly PositionLongHash[] longCastlingHashesTable;

        public ZobristHasher()
        {
            var random = new Random(RndSeed);
            pieceHashesTable = GeneratePieceHashesTable(random);
            playerToMoveHashesTable = GeneratePlayerToMoveHashesTable(random);
            enPassantCaptureColumnHashesTable = GenerateEnPassantCaptureColumnHashesTable(
                random);
            shortCastlingHashesTable = GenerateShortCastlingHashesTable(random);
            longCastlingHashesTable = GenerateLongCastlingHashesTable(random);
        }

        public PositionLongHash CalculateHash(Position.Position position)
        {
            var result = new PositionLongHash();
            result = result.CalculateXor(
                CalculatePiecesHash(
                    position.Board.GetPieces(Player.Black),
                    Player.Black));
            result = result.CalculateXor(
                CalculatePiecesHash(
                    position.Board.GetPieces(Player.White),
                    Player.White));
            result = result.CalculateXor(playerToMoveHashesTable[(int)position.PlayerToMove]);
            result = result.CalculateXor(
                GetEnPassantCaptureColumnHash(position.EnPassantCaptureColumn));
            result = result.CalculateXor(
                GetCastlingAvailabilityHash(
                    position.GetCastlingAvailability(Player.Black),
                    Player.Black));
            result = result.CalculateXor(
                GetCastlingAvailabilityHash(
                    position.GetCastlingAvailability(Player.White),
                    Player.White));
            return result;
        }

        private PositionLongHash CalculatePiecesHash(
            IEnumerable<PieceOnBoard> pieces,
            Player player)
        {
            var result = new PositionLongHash();
            foreach (var pieceOnBoard in pieces)
            {
                var pieceHash = GetPieceHash(player, pieceOnBoard.piece, pieceOnBoard.square);
                result = result.CalculateXor(pieceHash);
            }
            return result;
        }

        private PositionLongHash GetPieceHash(Player player, Piece piece, BoardSquare square)
        {
            return pieceHashesTable[CalculateTableIndex(player, piece, square)];
        }

        private PositionLongHash GetCastlingAvailabilityHash(
            CastlingAvailability castlingAvailability,
            Player player)
        {
            var result = new PositionLongHash();
            if (castlingAvailability.CanCastleShort)
            {
                result = result.CalculateXor(shortCastlingHashesTable[(int)player]);
            }
            if (castlingAvailability.CanCastleLong)
            {
                result = result.CalculateXor(longCastlingHashesTable[(int)player]);
            }
            return result;
        }

        private PositionLongHash GetEnPassantCaptureColumnHash(Option<int> column)
        {
            var columnIndex = column.ValueOr(Board.Board.ColumnCount);
            return enPassantCaptureColumnHashesTable[columnIndex];
        }

        private int CalculateTableIndex(Player player, Piece piece, BoardSquare square)
        {
            return (((int)player * PieceTypeCount) + (int)piece) * Board.Board.SquareCount
                + square.IntValue;
        }

        private static PositionLongHash[] GeneratePieceHashesTable(Random random)
        {
            var tableSize = PlayerCount * PieceTypeCount * Board.Board.SquareCount;
            return GenerateHashesTable(random, tableSize);
        }

        private static PositionLongHash[] GeneratePlayerToMoveHashesTable(Random random)
        {
            return GenerateHashesTable(random, tableSize: PlayerCount);
        }

        private static PositionLongHash[] GenerateEnPassantCaptureColumnHashesTable(
            Random random)
        {
            return GenerateHashesTable(random, tableSize: Board.Board.ColumnCount + 1);
        }

        private static PositionLongHash[] GenerateShortCastlingHashesTable(Random random)
        {
            return GenerateHashesTable(random, tableSize: PlayerCount);
        }

        private static PositionLongHash[] GenerateLongCastlingHashesTable(Random random)
        {
            return GenerateHashesTable(random, tableSize: PlayerCount);
        }

        private static PositionLongHash[] GenerateHashesTable(Random random, int tableSize)
        {
            var hashesTable = new PositionLongHash[tableSize];
            for (int i = 0; i < hashesTable.Length; ++i)
            {
                hashesTable[i] = GenerateLongHash(random);
            }
            return hashesTable;
        }

        private static PositionLongHash GenerateLongHash(Random random)
        {
            uint hash1 = random.GetNextUInt();
            uint hash2 = random.GetNextUInt();
            uint hash3 = random.GetNextUInt();
            uint hash4 = random.GetNextUInt();
            return new PositionLongHash(hash1, hash2, hash3, hash4);
        }
    }
}
