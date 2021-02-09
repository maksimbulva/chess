using ChessEngine.Board;
using ChessEngine.Fen;
using ChessEngine.Move.Generator;
using System;
using System.Collections.Generic;
using System.Linq;

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
            ResetGame(InitialPosition);
        }

        public bool ResetGame(string fenEcondedPosition)
        {
            try
            {
                // TODO: Check whether parsed position is valid (no pawns on 8th rank and so on)
                currentPosition = FenDecoder.Decode(fenEcondedPosition);
                return true;
            }
            catch (Exception)
            {
                return false;
            }
        }

        public IEnumerable<Move.Move> GetLegalMoves()
        {
            if (legalMoves == null)
            {
                legalMoves = GenerateLegalMoves();
            }
            return legalMoves;
        }

        public bool TryPlayMove(BoardSquare originSquare, BoardSquare destSquare)
        {
            var moveToPlay = GetLegalMoves()
                .Where(x => x.OriginSquare == originSquare && x.DestSquare == destSquare && !x.IsPromotion)
                .FirstOrDefault();
            return TryPlayMove(moveToPlay);
        }

        public bool TryPlayMove(BoardSquare originSquare, BoardSquare destSquare, Piece promoteToPiece)
        {
            var moveToPlay = GetLegalMoves()
                .Where(x =>
                {
                    return x.OriginSquare == originSquare &&
                        x.DestSquare == destSquare &&
                        x.IsPromotion &&
                        x.GetPromoteToPiece() == promoteToPiece;
                })
                .FirstOrDefault();
            return TryPlayMove(moveToPlay);
        }

        public bool TryUndoMove()
        {
            if (!currentPosition.HasMoveToUndo)
            {
                return false;
            }

            currentPosition.UndoMove();
            legalMoves = null;
            return true;
        }

        private List<Move.Move> GenerateLegalMoves() => MoveGenerator.GenerateLegalMoves(currentPosition);

        private bool TryPlayMove(Move.Move moveToPlay)
        {
            if (moveToPlay.IsNullMove)
            {
                return false;
            }

            currentPosition.PlayMove(moveToPlay);
            legalMoves = null;
            return true;
        }
    }
}
