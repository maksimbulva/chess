using ChessEngine.Board;
using System.Collections.Generic;

namespace ChessEngine
{
    public interface IChessEngine
    {
        Position.Position CurrentPosition { get; }

        void ResetGame();

        bool ResetGame(string fenEcondedPosition);

        IEnumerable<Move.Move> GetLegalMoves();

        bool TryPlayMove(BoardSquare originSquare, BoardSquare destSquare);

        bool TryPlayMove(BoardSquare originSquare, BoardSquare destSquare, Piece promoteToPiece);

        bool TryUndoMove();
    }
}
