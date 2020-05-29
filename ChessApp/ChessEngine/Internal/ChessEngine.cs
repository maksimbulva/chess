using ChessEngine.Board;
using ChessEngine.Search;
using System;
using System.Collections.Generic;
using System.Text;

namespace ChessEngine.Internal
{
    internal class ChessEngine : IChessEngine
    {
        public string Name => throw new NotImplementedException();

        public IGame Game => throw new NotImplementedException();

        public SearchInfo SearchInfo => throw new NotImplementedException();

        public Variation FindBestVariation(Action<SearchInfo> progressCallback)
        {
            throw new NotImplementedException();
        }

        public PlayerSettings GetPlayerSettings(Player player)
        {
            throw new NotImplementedException();
        }

        public SearchInfo GetSearchInfo()
        {
            throw new NotImplementedException();
        }

        public void PlayMove(Square originSquare, Square destSquare, Piece? promoteToPieceType = null)
        {
            throw new NotImplementedException();
        }

        public void PlayMove(string moveString)
        {
            throw new NotImplementedException();
        }

        public void ResetGame()
        {
            throw new NotImplementedException();
        }

        public void ResetGame(string positionFen)
        {
            throw new NotImplementedException();
        }
    }
}
