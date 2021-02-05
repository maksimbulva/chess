using ChessEngine.Board;
using System.Collections.Generic;

namespace ChessEngine
{
    public interface IChessEngine
    {
        void ResetGame();

        IEnumerable<Move.Move> GetLegalMoves();

        bool TryPlayMove(BoardSquare originSquare, BoardSquare destSquare);

        bool TryUndoMove();
    }
}
