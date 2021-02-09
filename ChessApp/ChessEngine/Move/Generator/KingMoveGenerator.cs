using ChessEngine.Board;
using System.Collections.Generic;
using static ChessEngine.Board.BoardAttackUtils;
using static ChessEngine.Board.Columns;
using static ChessEngine.Board.Rows;
using static ChessEngine.Internal.Utils;

namespace ChessEngine.Move.Generator
{
    internal sealed class KingMoveGenerator : IPieceMoveGenerator
    {
        private static readonly KingDeltasMoveGenerator deltasMoveGenerator = new KingDeltasMoveGenerator();

        private readonly Player player;
        private readonly Player otherPlayer;
        private readonly BoardSquare initialKingSquare;
        private readonly int initialKingRow;

        private readonly Move shortCastleMove;
        private readonly Move longCastleMove;

        public KingMoveGenerator(Player player)
        {
            this.player = player;
            otherPlayer = GetOtherPlayer(player);
            initialKingSquare = GetKingInitialSquare(player);
            initialKingRow = initialKingSquare.Row;

            shortCastleMove = CreateShortCastleMove();
            longCastleMove = CreateLongCastleMove();
        }

        public void AppendMoves(BoardSquare originSquare, Position.Position position, List<Move> moves)
        {
            deltasMoveGenerator.AppendMoves(originSquare, position, moves);
            AppendCastleMoves(position, moves);
        }

        private void AppendCastleMoves(Position.Position position, List<Move> moves)
        {
            var availabilityFlags = position.GetCastlingAvailability(player);
            if (!availabilityFlags.CanCastleShort && !availabilityFlags.CanCastleLong)
            {
                return;
            }

            var board = position.Board;
            if (IsBoardSquareAttacked(initialKingSquare, board, otherPlayer))
            {
                return;
            }

            if (availabilityFlags.CanCastleShort && IsLegalToCastleShort(board))
            {
                moves.Add(shortCastleMove);
            }
            if (availabilityFlags.CanCastleLong && IsLegalToCaslteLong(board))
            {
                moves.Add(longCastleMove);
            }
        }

        private bool IsLegalToCastleShort(Board.Board board)
        {
            return IsAllSquaresAlongCastleAreEmpty(ColumnF, ColumnG, board) &&
                IsNoneSquareAlongCastleIsAttacked(ColumnF, ColumnG, board);
        }

        private bool IsLegalToCaslteLong(Board.Board board)
        {
            return IsAllSquaresAlongCastleAreEmpty(ColumnB, ColumnD, board) &&
                IsNoneSquareAlongCastleIsAttacked(ColumnC, ColumnD, board);
        }

        private bool IsAllSquaresAlongCastleAreEmpty(int column1, int column2, Board.Board board)
        {
            for (int column = column1; column <= column2; ++column)
            {
                if (!board.IsEmpty(initialKingRow, column))
                {
                    return false;
                }
            }
            return true;
        }

        private bool IsNoneSquareAlongCastleIsAttacked(int column1, int column2, Board.Board board)
        {
            for (int column = column1; column <= column2; ++column)
            {
                var square = new BoardSquare(initialKingRow, column);
                if (IsBoardSquareAttacked(square, board, otherPlayer))
                {
                    return false;
                }
            }
            return true;
        }

        private Move CreateShortCastleMove()
        {
            return new MoveBuilder(Piece.King, initialKingSquare)
                .SetDestSquare(GetShortCastleDestSquare())
                .SetShortCastle()
                .Build();
        }

        private Move CreateLongCastleMove()
        {
            return new MoveBuilder(Piece.King, initialKingSquare)
                .SetDestSquare(GetLongCastleDestSquare())
                .SetLongCastle()
                .Build();
        }

        private BoardSquare GetShortCastleDestSquare()
        {
            return new BoardSquare(initialKingRow, ColumnG);
        }

        private BoardSquare GetLongCastleDestSquare()
        {
            return new BoardSquare(initialKingRow, ColumnC);
        }

        private static BoardSquare GetKingInitialSquare(Player player)
        {
            return new BoardSquare(GetInitialRow(player), ColumnE);
        }

        private sealed class KingDeltasMoveGenerator : BaseDeltasMoveGenerator
        {
            private static readonly MoveDelta[] KingDeltas = new MoveDelta[] {
                new MoveDelta(-1, -1),
                new MoveDelta(0, -1),
                new MoveDelta(1, -1),
                new MoveDelta(-1, 0),
                new MoveDelta(1, 0),
                new MoveDelta(-1, 1),
                new MoveDelta(0, 1),
                new MoveDelta(1, 1)
            };

            protected override MoveDelta[] GetMoveDeltas() => KingDeltas;

            protected override Piece GetPiece() => Piece.King;
        }
    }
}
