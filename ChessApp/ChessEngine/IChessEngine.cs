using ChessEngine.Board;
using ChessEngine.Search;
using System;

namespace ChessEngine
{
    public interface IChessEngine
    {
        string Name { get; }

        IGame Game { get; }

        SearchInfo SearchInfo { get; }

        PlayerSettings GetPlayerSettings(Player player);

        void ResetGame();

        void ResetGame(string positionFen);

        void PlayMove(Square originSquare, Square destSquare, Piece? promoteToPieceType = null);

        void PlayMove(string moveString);

        Variation FindBestVariation(Action<SearchInfo> progressCallback);
    }
}
