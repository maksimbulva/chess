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
        private readonly List<PositionHistoryItem> history = new List<PositionHistoryItem>();

        public Board.Board Board { get; }
        public Player PlayerToMove { get; private set; }
        public int HalfMoveClock { get; }
        public int FullMoveNumber { get; }

        public Option<int> EnPassantCaptureColumn { get; private set; }

        public bool IsCanCaptureEnPassant => EnPassantCaptureColumn.HasValue;

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
            this.EnPassantCaptureColumn = enPassantCaptureColumn;
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
            history.Add(new PositionHistoryItem(
                move: legalMove,
                enPassantCaptureColumn: EnPassantCaptureColumn));

            Board.PlayMove(legalMove);
            PlayerToMove = GetOtherPlayer(PlayerToMove);
            EnPassantCaptureColumn = GetNewEnPassantCaptureColumn(legalMove);
        }

        internal void UndoMove()
        {
            var stateToRestore = history.Last();
            history.RemoveAt(history.Count - 1);

            PlayerToMove = GetOtherPlayer(PlayerToMove);
            Board.UndoMove(stateToRestore.Move, PlayerToMove);
            EnPassantCaptureColumn = stateToRestore.EnPassantCaptureColumn;
        }

        private static Option<int> GetNewEnPassantCaptureColumn(Move.Move movePlayed)
        {
            if (movePlayed.IsPawnDoubleMove)
            {
                return Option.Some(movePlayed.OriginSquare.Column);
            }
            return Option.None<int>();
        }
    }
}
