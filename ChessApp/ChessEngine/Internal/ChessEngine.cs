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

        private Position.Position _currentPosition;
        private Position.Position CurrentPosition
        {
            get => _currentPosition;
            set
            {
                legalMoves = null;
                _currentPosition = value;
            }
        }

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
                CurrentPosition = FenDecoder.Decode(fenEcondedPosition);
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
            if (!CurrentPosition.HasMoveToUndo)
            {
                return false;
            }

            CurrentPosition.UndoMove();
            legalMoves = null;
            return true;
        }

        private List<Move.Move> GenerateLegalMoves() => MoveGenerator.GenerateLegalMoves(CurrentPosition);

        private bool TryPlayMove(Move.Move moveToPlay)
        {
            if (moveToPlay.IsNullMove)
            {
                return false;
            }

            CurrentPosition.PlayMove(moveToPlay);
            legalMoves = null;
            return true;
        }
    }
}
