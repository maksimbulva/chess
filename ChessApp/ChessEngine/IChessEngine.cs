using System.Collections.Generic;

namespace ChessEngine
{
    public interface IChessEngine
    {
        void ResetGame();

        IEnumerable<Move.Move> GetLegalMoves();
    }
}
