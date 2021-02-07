using System;
using System.Collections.Generic;

namespace ChessEngine.Move.Generator
{
    internal static class PieceMoveGeneratorFactory
    {
        public static readonly MoveDelta[] BishopRayDeltas = new MoveDelta[]
        {
            new MoveDelta(-1, -1),
            new MoveDelta(-1, 1),
            new MoveDelta(1, -1),
            new MoveDelta(1, 1)
        };

        public static readonly MoveDelta[] RookRayDeltas = new MoveDelta[]
        {
            new MoveDelta(-1, 0),
            new MoveDelta(0, -1),
            new MoveDelta(0, 1),
            new MoveDelta(1, 0)
        };

        private static readonly PawnMoveGenerator blackPawnMoveGenerator =
            new PawnMoveGenerator(Player.Black);

        private static readonly PawnMoveGenerator whitePawnMoveGenerator =
            new PawnMoveGenerator(Player.White);

        private static readonly KnightMoveGenerator knightMoveGenerator =
            new KnightMoveGenerator();

        private static readonly RayMoveGenerator bishopMoveGenerator =
            new RayMoveGenerator(Piece.Bishop, BishopRayDeltas);

        private static readonly RayMoveGenerator rookMoveGenerator =
            new RayMoveGenerator(Piece.Rook, RookRayDeltas);

        private static readonly RayMoveGenerator queenMoveGenerator =
            new RayMoveGenerator(Piece.Queen, GenerateQueenRayDeltas());

        private static readonly KingMoveGenerator kingMoveGenerator =
            new KingMoveGenerator();

        public static IPieceMoveGenerator GetPieceMoveGenerator(Player player, Piece piece)
        {
            switch (piece)
            {
                case Piece.Pawn:
                    return GetPawnMoveGenerator(player);
                case Piece.Knight:
                    return knightMoveGenerator;
                case Piece.Bishop:
                    return bishopMoveGenerator;
                case Piece.Rook:
                    return rookMoveGenerator;
                case Piece.Queen:
                    return queenMoveGenerator;
                case Piece.King:
                    return kingMoveGenerator;
                default:
                    throw new ArgumentException($"Unexpeced piece {piece}");
            }
        }

        private static PawnMoveGenerator GetPawnMoveGenerator(Player player)
        {
            return (player == Player.Black) ? blackPawnMoveGenerator : whitePawnMoveGenerator;
        }

        private static MoveDelta[] GenerateQueenRayDeltas()
        {
            var rayDeltas = new List<MoveDelta>(BishopRayDeltas);
            rayDeltas.AddRange(RookRayDeltas);
            return rayDeltas.ToArray();
        }
    }
}
