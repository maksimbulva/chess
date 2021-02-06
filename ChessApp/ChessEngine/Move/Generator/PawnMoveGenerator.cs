using ChessEngine.Board;
using System;
using System.Collections.Generic;
using System.Text;

namespace ChessEngine.Move.Generator
{
    internal sealed class PawnMoveGenerator : IPieceMoveGenerator
    {
        private readonly Player player;
        private readonly int initialRow;
        private readonly int deltaRow;

        public PawnMoveGenerator(Player player)
        {
            this.player = player;
            initialRow = GetInitialRow(player);
            deltaRow = GetDeltaRow(player);
        }

        public void AppendMoves(BoardSquare originSquare, Position.Position position, List<Move> moves)
        {
            var board = position.Board;
            var moveBuilder = new MoveBuilder(Piece.Pawn, originSquare);

            var moveForwardSquare = new BoardSquare(originSquare.Row + deltaRow, originSquare.Column);
            if (board.IsEmpty(moveForwardSquare))
            {
                moves.Add(moveBuilder.SetDestSquare(moveForwardSquare).Build());
                if (originSquare.Row == initialRow)
                {
                    var moveDoubleForwardSquare = GetDoubleForwardMoveSquare(originSquare);
                    if (board.IsEmpty(moveDoubleForwardSquare))
                    {
                        moves.Add(moveBuilder.SetDestSquare(moveDoubleForwardSquare).Build());
                    }
                }
            }

            if (originSquare.Column > 0)
            {
                AppendIfCanCapture(
                    new BoardSquare(originSquare.Row + deltaRow, originSquare.Column - 1),
                    moveBuilder,
                    board,
                    moves);
            }
            if (originSquare.Column + 1 < Board.Board.ColumnCount)
            {
                AppendIfCanCapture(
                    new BoardSquare(originSquare.Row + deltaRow, originSquare.Column + 1),
                    moveBuilder,
                    board,
                    moves);
            }
        }

        private void AppendIfCanCapture(
            BoardSquare destSquare,
            MoveBuilder moveBuilder,
            Board.Board board,
            List<Move> moves)
        {
            if (board.IsEmpty(destSquare))
            {
                return;
            }

            var pieceAtDestSquare = board.GetPieceAt(destSquare);
            if (pieceAtDestSquare.player != player)
            {
                moves.Add(moveBuilder
                    .SetDestSquare(destSquare)
                    .SetCapture(pieceAtDestSquare.piece)
                    .Build());
            }
        }

        private BoardSquare GetDoubleForwardMoveSquare(BoardSquare originSquare)
        {
            return new BoardSquare(originSquare.Row + deltaRow * 2, originSquare.Column);
        }

        private static int GetInitialRow(Player player) => player == Player.Black ? 6 : 1;

        private static int GetDeltaRow(Player player) => player == Player.Black ? -1 : 1;
    }
}
