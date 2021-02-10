using System.Collections.Generic;
using System.Linq;
using static ChessEngine.Board.BoardAttackUtils;
using static ChessEngine.Internal.Utils;

namespace ChessEngine.Move.Generator
{
    internal static class MoveGenerator
    {
        public static List<Move> GenerateLegalMoves(Position.Position position)
        {
            return GeneratePseudoLegalMoves(position).Where(x => IsLegalMove(position, x)).ToList();
        }

        internal static List<Move> GeneratePseudoLegalMoves(Position.Position position)
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
            if (move.IsCapture && move.GetCapturedPiece() == Piece.King)
            {
                throw new System.Exception();
            }
            var myPlayer = position.PlayerToMove;
            var board = position.Board;
            board.PlayMove(move, myPlayer);
            var myKingSquare = board.GetKingSquare(myPlayer);
            var myKingBecameAttacked = IsBoardSquareAttacked(
                myKingSquare,
                board,
                GetOtherPlayer(position.PlayerToMove));
            board.UndoMove(move, position.PlayerToMove);
            return !myKingBecameAttacked;
        }
    }
}
