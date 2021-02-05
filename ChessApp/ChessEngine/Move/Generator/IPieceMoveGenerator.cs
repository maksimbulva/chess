using ChessEngine.Board;
using System.Collections.Generic;

namespace ChessEngine.Move.Generator
{
    internal interface IPieceMoveGenerator
    {
        void AppendMoves(
            BoardSquare originSquare,
            Position.Position position,
            List<Move> moves);
    }
}
