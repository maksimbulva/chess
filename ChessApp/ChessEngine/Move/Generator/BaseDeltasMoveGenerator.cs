using ChessEngine.Board;
using System.Collections.Generic;

namespace ChessEngine.Move.Generator
{
    internal abstract class BaseDeltasMoveGenerator : IPieceMoveGenerator
    {
        public void AppendMoves(BoardSquare originSquare, Position.Position position, List<Move> moves)
        {
            var board = position.Board;
            var moveDeltas = GetMoveDeltas();
            var moveBuilder = new MoveBuilder(GetPiece(), originSquare);

            for (int i = 0; i < moveDeltas.Length; ++i)
            {
                if (!moveDeltas[i].IsCanApplyTo(originSquare))
                {
                    continue;
                }
                var destSquare = moveDeltas[i].GetDestSquare(originSquare);
                if (board.IsEmpty(destSquare))
                {
                    moves.Add(moveBuilder.SetDestSquare(destSquare).Build());
                }
                else
                {
                    // TODO: Proceed with captures
                }
            }
        }

        protected abstract MoveDelta[] GetMoveDeltas();

        protected abstract Piece GetPiece();
    }
}
