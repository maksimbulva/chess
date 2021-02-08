using ChessEngine.Board;
using Optional;
using System.Collections.Generic;
using System.Linq;
using static ChessEngine.Internal.Utils;

namespace ChessEngine.Position
{
    public sealed class Position
    {
        private static readonly BoardSquare[] shortCastleRookInitialSquares = GetShortCastleRookInitialSquares();
        private static readonly BoardSquare[] longCastleRookInitialSquares = GetLongCastleRookInitialSquares();

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
            EnPassantCaptureColumn = enPassantCaptureColumn;
            HalfMoveClock = halfMoveClock;
            FullMoveNumber = fullMoveNumber;

            this.whiteCastlingAvailability = OptimizeCastlingAvailability(
                whiteCastlingAvailability,
                Player.White);

            this.blackCastlingAvailability = OptimizeCastlingAvailability(
                blackCastlingAvailability,
                Player.Black);
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
                legalMove,
                EnPassantCaptureColumn,
                whiteCastlingAvailability,
                blackCastlingAvailability));

            Board.PlayMove(legalMove);
            UpdateCastlingAvailability(legalMove);

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
            whiteCastlingAvailability = stateToRestore.WhiteCastlingAvailability;
            blackCastlingAvailability = stateToRestore.BlackCastlingAvailability;
        }

        private void UpdateCastlingAvailability(Move.Move move)
        {
            if (PlayerToMove == Player.Black)
            {
                blackCastlingAvailability = UpdateCastlingAvailability(
                    blackCastlingAvailability,
                    move,
                    Player.Black);
            }
            else
            {
                whiteCastlingAvailability = UpdateCastlingAvailability(
                    whiteCastlingAvailability,
                    move,
                    Player.White);
            }
        }

        private CastlingAvailability OptimizeCastlingAvailability(
            CastlingAvailability currentFlags,
            Player player)
        {
            if (!IsKingAtInitialSquare(player))
            {
                return CastlingAvailability.None;
            }

            var canCastleShort = currentFlags.CanCastleShort &&
                IsRookAtInitialPositionForShortCastle(player);

            var canCastleLong = currentFlags.CanCastleLong &&
                IsRookAtInitialPositionForLongCastle(player);

            return new CastlingAvailability(canCastleShort, canCastleLong);
        }

        private bool IsKingAtInitialSquare(Player player)
        {
            var initialKingSquare = GetInitialKingSquare(player);
            if (Board.IsEmpty(initialKingSquare))
            {
                return false;
            }
            var pieceAtKingSquare = Board.GetPieceAt(initialKingSquare);
            return pieceAtKingSquare.player == player && pieceAtKingSquare.piece == Piece.King;
        }

        private bool IsRookAtInitialPositionForShortCastle(Player player)
        {
            return IsRookAt(shortCastleRookInitialSquares[(int)player], player);
        }

        private bool IsRookAtInitialPositionForLongCastle(Player player)
        {
            return IsRookAt(longCastleRookInitialSquares[(int)player], player);
        }

        private bool IsRookAt(BoardSquare initialRookSquare, Player player)
        {
            if (Board.IsEmpty(initialRookSquare))
            {
                return false;
            }
            var pieceAtRookSquare = Board.GetPieceAt(initialRookSquare);
            return pieceAtRookSquare.player == player && pieceAtRookSquare.piece == Piece.Rook;
        }

        private static Option<int> GetNewEnPassantCaptureColumn(Move.Move movePlayed)
        {
            if (movePlayed.IsPawnDoubleMove)
            {
                return Option.Some(movePlayed.OriginSquare.Column);
            }
            return Option.None<int>();
        }

        private static CastlingAvailability UpdateCastlingAvailability(
            CastlingAvailability currentFlags,
            Move.Move move,
            Player player)
        {
            if (!currentFlags.CanCastle ||
                move.Piece == Piece.King ||
                move.IsShortCastle ||
                move.IsLongCastle)
            {
                return CastlingAvailability.None;
            }

            if (move.Piece != Piece.Rook)
            {
                return currentFlags;
            }

            var canCaslteShort = currentFlags.CanCastleShort &&
                move.OriginSquare != shortCastleRookInitialSquares[(int)player];
            var canCastleLong = currentFlags.CanCastleLong &&
                move.OriginSquare != longCastleRookInitialSquares[(int)player];
            return new CastlingAvailability(canCaslteShort, canCastleLong);
        }

        private static BoardSquare[] GetShortCastleRookInitialSquares()
        {
            return GetRookInitialSquares(7);
        }

        private static BoardSquare[] GetLongCastleRookInitialSquares()
        {
            return GetRookInitialSquares(0);
        }

        private static BoardSquare[] GetRookInitialSquares(int column)
        {
            var result = new BoardSquare[2];
            result[(int)Player.Black] = new BoardSquare(7, column);
            result[(int)Player.White] = new BoardSquare(0, column);
            return result;
        }

        private static BoardSquare GetInitialKingSquare(Player player)
        {
            var initialRow = player == Player.Black ? 7 : 0;
            return new BoardSquare(initialRow, 4);
        }
    }
}
