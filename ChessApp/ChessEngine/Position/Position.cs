using ChessEngine.Board;
using Optional;
using System;
using System.Collections.Generic;
using System.Text;

namespace ChessEngine.Position
{
    public sealed class Position
    {
        private CastlingAvailability _whiteCastlingAvailability;
        private CastlingAvailability _blackCastlingAvailability;
        private Option<int> _enPassantCaptureColumn;

        public Board.Board Board { get; }
        public Player PlayerToMove { get; private set; }
        public int HalfMoveClock { get; }
        public int FullMoveNumber { get; }

        public bool IsCanCaptureEnPassant => _enPassantCaptureColumn.HasValue;

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
            _whiteCastlingAvailability = whiteCastlingAvailability;
            _blackCastlingAvailability = blackCastlingAvailability;
            _enPassantCaptureColumn = enPassantCaptureColumn;
            HalfMoveClock = halfMoveClock;
            FullMoveNumber = fullMoveNumber;
        }

        public CastlingAvailability GetCastlingAvailability(Player player)
        {
            if (player == Player.Black)
            {
                return _blackCastlingAvailability;
            }
            else
            {
                return _whiteCastlingAvailability;
            }
        }
    }
}
