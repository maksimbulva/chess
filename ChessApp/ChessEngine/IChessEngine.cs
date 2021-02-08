using ChessEngine.Board;
using System.Collections.Generic;

namespace ChessEngine
{
    public interface IChessEngine
    {
        void ResetGame();

        bool ResetGame(string fenEcondedPosition);

        IEnumerable<Move.Move> GetLegalMoves();

        bool TryPlayMove(BoardSquare originSquare, BoardSquare destSquare);

        bool TryUndoMove();
    }
}
