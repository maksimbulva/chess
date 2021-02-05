using ChessEngine.Board;
using Optional;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using static ChessEngine.Internal.Utils;

namespace ChessEngine.Position
{
    public sealed class Position
    {
        private CastlingAvailability whiteCastlingAvailability;
        private CastlingAvailability blackCastlingAvailability;
        private Option<int> enPassantCaptureColumn;
        private readonly List<Move.Move> history = new List<Move.Move>();

        public Board.Board Board { get; }
        public Player PlayerToMove { get; private set; }
        public int HalfMoveClock { get; }
        public int FullMoveNumber { get; }

        public bool IsCanCaptureEnPassant => enPassantCaptureColumn.HasValue;

        public bool HasMoveToUndo => history.Count > 0;

        internal Position(
            List<PieceOnBoard> pieces,
            Player playerToMove,
            CastlingAvailability whiteCastlingAvailability,
            CastlingAvailability blackCastlingAvailability,
            Option<int> enPassantCaptureColumn,
            int halfMoveClock,
            int fullMoveNumber)
        {
            Board = new Board.Board(pieces);
            PlayerToMove = playerToMove;
            this.whiteCastlingAvailability = whiteCastlingAvailability;
            this.blackCastlingAvailability = blackCastlingAvailability;
            this.enPassantCaptureColumn = enPassantCaptureColumn;
            HalfMoveClock = halfMoveClock;
            FullMoveNumber = fullMoveNumber;
        }

        public CastlingAvailability GetCastlingAvailability(Player player)
        {
            if (player == Player.Black)
            {
                return blackCastlingAvailability;
            }
            else
            {
                return whiteCastlingAvailability;
            }
        }

        internal void PlayMove(Move.Move legalMove)
        {
            Board.PlayMove(legalMove);
            PlayerToMove = GetOtherPlayer(PlayerToMove);
            history.Add(legalMove);
        }

        internal void UndoMove()
        {
            var moveToUndo = history.Last();
            history.RemoveAt(history.Count - 1);

            Board.UndoMove(moveToUndo);
            PlayerToMove = GetOtherPlayer(PlayerToMove);
        }
    }
}
