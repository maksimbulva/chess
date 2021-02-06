using System;
using System.Collections.Generic;
using System.Linq;

namespace ChessEngine.Move.Generator
{
    internal static class MoveGenerator
    {
        public static List<Move> GenerateLegalMoves(Position.Position position)
        {
            return GeneratePseudoLegalMoves(position).Where(x => IsLegalMove(position, x)).ToList();
        }

        private static List<Move> GeneratePseudoLegalMoves(Position.Position position)
        {
            var moves = new List<Move>(capacity: 32);
            foreach (var pieceOnBoard in position.Board.GetPieces(position.PlayerToMove))
            {
                PieceMoveGeneratorFactory.GetPieceMoveGenerator(position.PlayerToMove, pieceOnBoard.piece)
                    .AppendMoves(pieceOnBoard.square, position, moves);
            }
            return moves;
        }

        private static bool IsLegalMove(Position.Position position, Move move)
        {
            // TODO: Implement me
            return true;
        }
    }
}
