using ChessEngine.Board;
using System;
using System.Collections.Generic;
using static System.Math;

namespace ChessEngine.Move.Generator
{
    internal sealed class PawnMoveGenerator : IPieceMoveGenerator
    {
        private readonly Player player;
        private readonly int initialRow;
        private readonly int prePromotionRow;
        private readonly int deltaRow;
        private readonly int enPassantCaptureRow;

        public PawnMoveGenerator(Player player)
        {
            this.player = player;
            initialRow = GetInitialRow(player);
            prePromotionRow = GetPrePromotionRow(player);
            deltaRow = GetDeltaRow(player);
            enPassantCaptureRow = GetEnPassantCaptureRow(player);
        }

        public void AppendMoves(BoardSquare originSquare, Position.Position position, List<Move> moves)
        {
            var board = position.Board;
            var moveBuilder = new MoveBuilder(Piece.Pawn, originSquare);
            var moveAppender = GetMoveAppender(originSquare);

            var moveForwardSquare = new BoardSquare(originSquare.Row + deltaRow, originSquare.Column);
            if (board.IsEmpty(moveForwardSquare))
            {
                moveAppender(moveBuilder.SetDestSquare(moveForwardSquare), moves);
                if (originSquare.Row == initialRow)
                {
                    var moveDoubleForwardSquare = GetDoubleForwardMoveSquare(originSquare);
                    if (board.IsEmpty(moveDoubleForwardSquare))
                    {
                        moves.Add(moveBuilder
                            .SetDestSquare(moveDoubleForwardSquare)
                            .SetPawnDoubleMove()
                            .Build());
                    }
                }
            }

            if (originSquare.Column > 0)
            {
                AppendIfCanCapture(
                    new BoardSquare(originSquare.Row + deltaRow, originSquare.Column - 1),
                    moveBuilder,
                    board,
                    moves,
                    moveAppender);
            }
            if (originSquare.Column + 1 < Board.Board.ColumnCount)
            {
                AppendIfCanCapture(
                    new BoardSquare(originSquare.Row + deltaRow, originSquare.Column + 1),
                    moveBuilder,
                    board,
                    moves,
                    moveAppender);
            }

            if (originSquare.Row == enPassantCaptureRow &&
                position.EnPassantCaptureColumn.Exists(x => Abs(originSquare.Column - x) == 1))
            {
                var enPassantDstSquare = new BoardSquare(
                    originSquare.Row + deltaRow,
                    position.EnPassantCaptureColumn.ValueOr(0));
                moves.Add(moveBuilder
                    .SetDestSquare(enPassantDstSquare)
                    .SetEnPassantCapture()
                    .Build());
            }
        }

        private void AppendIfCanCapture(
            BoardSquare destSquare,
            MoveBuilder moveBuilder,
            Board.Board board,
            List<Move> moves,
            Action<MoveBuilder, List<Move>> moveAppender)
        {
            if (board.IsEmpty(destSquare))
            {
                return;
            }

            var pieceAtDestSquare = board.GetPieceAt(destSquare);
            if (pieceAtDestSquare.player != player)
            {
                moveAppender(
                    moveBuilder.SetDestSquare(destSquare).SetCapture(pieceAtDestSquare.piece),
                    moves);
            }
        }

        private BoardSquare GetDoubleForwardMoveSquare(BoardSquare originSquare)
        {
            return new BoardSquare(originSquare.Row + deltaRow * 2, originSquare.Column);
        }

        private Action<MoveBuilder, List<Move>> GetMoveAppender(BoardSquare square)
        {
            if (square.Row == prePromotionRow)
            {
                return AppendMoveWithPromotions;
            }
            else
            {
                return AppendMoveAsIs;
            }
        }

        private static int GetInitialRow(Player player) => player == Player.Black ? 6 : 1;

        private static int GetPrePromotionRow(Player player) => player == Player.Black ? 1 : 6;

        private static int GetDeltaRow(Player player) => player == Player.Black ? -1 : 1;

        private static int GetEnPassantCaptureRow(Player player)
        {
            return player == Player.Black ? 3 : 4;
        }

        private static void AppendMoveAsIs(MoveBuilder moveBuilder, List<Move> moves)
        {
            moves.Add(moveBuilder.Build());
        }

        private static void AppendMoveWithPromotions(MoveBuilder moveBuilder, List<Move> moves)
        {
            moves.Add(moveBuilder.SetPromotion(Piece.Queen).Build());
            moves.Add(moveBuilder.SetPromotion(Piece.Rook).Build());
            moves.Add(moveBuilder.SetPromotion(Piece.Bishop).Build());
            moves.Add(moveBuilder.SetPromotion(Piece.Knight).Build());
        }
    }
}
