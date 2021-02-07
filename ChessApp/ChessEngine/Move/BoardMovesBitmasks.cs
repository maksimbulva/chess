using ChessEngine.Bitmask;
using ChessEngine.Board;
using static ChessEngine.Move.Generator.PieceMoveGeneratorFactory;

namespace ChessEngine.Move
{
    internal static class BoardMovesBitmasks
    {
        private static readonly Bitmask64[] bishopBitmaskTable = GenerateMoveBitmaskTable(Piece.Bishop);
        private static readonly Bitmask64[] rookBitmaskTable = GenerateMoveBitmaskTable(Piece.Rook);

        public static Bitmask64 GetBishopBitmask(BoardSquare square)
        {
            return bishopBitmaskTable[square.IntValue];
        }

        public static Bitmask64 GetRookBitmask(BoardSquare square)
        {
            return rookBitmaskTable[square.IntValue];
        }

        private static Bitmask64[] GenerateMoveBitmaskTable(Piece piece)
        {
            var result = new Bitmask64[Board.Board.SquareCount];
            for (int i = 0; i < result.Length; ++i)
            {
                result[i] = GenerateMovesBitmask(piece, new BoardSquare(i));
            }
            return result;
        }

        private static Bitmask64 GenerateMovesBitmask(Piece piece, BoardSquare square)
        {
            var result = new Bitmask64();
            if (piece == Piece.Bishop && square.IntValue == 16)
            {
                result = new Bitmask64();
            }
            var rayDeltas = piece == Piece.Bishop ? BishopRayDeltas : RookRayDeltas;
            foreach (var rayDelta in rayDeltas)
            {
                result = new Bitmask64(result.Value | RayBitmask(rayDelta, square).Value);
            }
            return result;
        }

        private static Bitmask64 RayBitmask(MoveDelta rayDelta, BoardSquare rayOrigin)
        {
            var result = new Bitmask64();
            var currentSquare = rayOrigin;
            while (rayDelta.IsCanApplyTo(currentSquare))
            {
                currentSquare = rayDelta.GetDestSquare(currentSquare);
                result.SetBit(currentSquare.IntValue);
            }
            return result;
        }
    }
}
