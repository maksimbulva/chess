using ChessEngine.Fen;
using ChessEngine.Move.Generator;
using System;
using System.Collections.Generic;

namespace ChessEngine.Internal
{
    internal sealed class ChessEngine : IChessEngine
    {
        private const string InitialPosition = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

        private Position.Position currentPosition;

        private List<Move.Move> legalMoves;

        public ChessEngine()
        {
            ResetGame();
        }

        public void ResetGame()
        {
            currentPosition = FenDecoder.Decode(InitialPosition);
        }

        public IEnumerable<Move.Move> GetLegalMoves()
        {
            if (legalMoves == null)
            {
                legalMoves = GenerateLegalMoves();
            }
            return legalMoves;
        }

        private List<Move.Move> GenerateLegalMoves() => MoveGenerator.GenerateLegalMoves(currentPosition);
    }
}
