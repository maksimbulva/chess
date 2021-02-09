using ChessEngine.Board;
using System.Collections.Generic;

namespace ChessEngine.Move.Generator
{
    internal sealed class RayMoveGenerator : IPieceMoveGenerator
    {
        private readonly Piece piece;
        private readonly MoveDelta[] rayDeltas;

        public RayMoveGenerator(Piece piece, MoveDelta[] rayDeltas)
        {
            this.piece = piece;
            this.rayDeltas = rayDeltas;
        }

        public void AppendMoves(BoardSquare originSquare, Position.Position position, List<Move> moves)
        {
            for (int i = 0; i < rayDeltas.Length; ++i)
            {
                AppendRayMoves(originSquare, position, rayDeltas[i], moves);
            }
        }

        private void AppendRayMoves(
            BoardSquare originSquare,
            Position.Position position,
            MoveDelta rayDelta,
            List<Move> moves)
        {
            var board = position.Board;
            var moveBuilder = new MoveBuilder(piece, originSquare);

            var currentSquare = originSquare;
            while (rayDelta.IsCanApplyTo(currentSquare))
            {
                currentSquare = rayDelta.GetDestSquare(currentSquare);
                if (board.IsEmpty(currentSquare))
                {
                    moves.Add(moveBuilder.SetDestSquare(currentSquare).Build());
                }
                else
                {
                    var obstacle = board.GetPieceAt(currentSquare);
                    if (obstacle.player != position.PlayerToMove)
                    {
                        moves.Add(moveBuilder
                            .SetDestSquare(currentSquare)
                            .SetCapture(obstacle.piece)
                            .Build());
                    }
                    break;
                }
            }
        }
    }
}
