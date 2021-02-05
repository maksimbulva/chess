using System;
using System.Collections.Generic;
using System.Linq;

namespace ChessEngine.Move.Generator
{
    internal static class MoveGenerator
    {
        private static readonly PawnMoveGenerator blackPawnMoveGenerator =
            new PawnMoveGenerator(Player.Black);

        private static readonly PawnMoveGenerator whitePawnMoveGenerator =
            new PawnMoveGenerator(Player.White);

        private static readonly KnightMoveGenerator knightMoveGenerator =
            new KnightMoveGenerator();

        public static List<Move> GenerateLegalMoves(Position.Position position)
        {
            return GeneratePseudoLegalMoves(position).Where(x => IsLegalMove(position, x)).ToList();
        }

        private static List<Move> GeneratePseudoLegalMoves(Position.Position position)
        {
            var moves = new List<Move>(capacity: 32);
            foreach (var pieceOnBoard in position.Board.GetPieces(position.PlayerToMove))
            {
                // TODO: Remove null check
                GetPieceMoveGenerator(position.PlayerToMove, pieceOnBoard.piece)
                    ?.AppendMoves(pieceOnBoard.square, position, moves);
            }
            return moves;
        }

        private static bool IsLegalMove(Position.Position position, Move move)
        {
            // TODO: Implement me
            return true;
        }

        private static IPieceMoveGenerator GetPieceMoveGenerator(
            Player player,
            Piece piece)
        {
            switch (piece)
            {
                case Piece.Pawn:
                    return GetPawnMoveGenerator(player);
                case Piece.Knight:
                    return knightMoveGenerator;
                default:
                    // TODO: Implement me
                    return null;
            }
        }

        private static PawnMoveGenerator GetPawnMoveGenerator(Player player)
        {
            return (player == Player.Black) ? blackPawnMoveGenerator : whitePawnMoveGenerator;
        }
    }
}
