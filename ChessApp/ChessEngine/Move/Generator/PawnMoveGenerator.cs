using ChessEngine.Board;
using System;
using System.Collections.Generic;
using System.Text;

namespace ChessEngine.Move.Generator
{
    internal sealed class PawnMoveGenerator : IPieceMoveGenerator
    {
        private readonly int initialRow;
        private readonly int deltaRow;

        public PawnMoveGenerator(Player player)
        {
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
        }

        private BoardSquare GetDoubleForwardMoveSquare(BoardSquare originSquare)
        {
            return new BoardSquare(originSquare.Row + deltaRow * 2, originSquare.Column);
        }

        private static int GetInitialRow(Player player) => player == Player.Black ? 6 : 1;

        private static int GetDeltaRow(Player player) => player == Player.Black ? -1 : 1;
    }
}
