using ChessEngine.Bitmask;
using ChessEngine.Move;
using System;
using static ChessEngine.Board.BoardBitmasks;
using static ChessEngine.Move.BoardMovesBitmasks;
using static System.Math;

namespace ChessEngine.Board
{
    internal static class BoardAttackUtils
    {
        private static readonly Bitmask64[] bimasksInBetweenRows = GenerateBimasksInBetweenIndices(GetRowBitmask);
        private static readonly Bitmask64[] bimasksInBetweenColumns = GenerateBimasksInBetweenIndices(GetColumnBitmask);

        public static bool IsBoardSquareAttacked(
            BoardSquare targetSquare,
            Board board,
            Player attacker)
        {
            var targetBishopMovesBitmask = GetBishopBitmask(targetSquare);
            var targetRookMovesBitmask = GetRookBitmask(targetSquare);

            long freeSquares = ~board.OccupiedSquares.Value;

            // TODO: Check pawn, knight and king attacks
            foreach (var attackingPiece in board.GetPieces(attacker))
            {
                switch (attackingPiece.piece)
                {
                    case Piece.Knight:
                        {
                            if (IsKnightDelta(new MoveDelta(targetSquare, attackingPiece.square)))
                            {
                                return true;
                            }
                        }
                        break;
                    case Piece.Bishop:
                        {
                            if (IsBishopAttack(
                                targetBishopMovesBitmask,
                                targetSquare,
                                attackingPiece.square,
                                freeSquares))
                            {
                                return true;
                            }
                        }
                        break;
                    case Piece.Rook:
                        {
                            if (IsRookAttack(
                                targetRookMovesBitmask,
                                targetSquare,
                                attackingPiece.square,
                                freeSquares))
                            {
                                return true;
                            }
                        }
                        break;
                    case Piece.Queen:
                        {
                            if (IsBishopAttack(
                                targetBishopMovesBitmask,
                                targetSquare,
                                attackingPiece.square,
                                freeSquares))
                            {
                                return true;
                            }
                            if (IsRookAttack(
                                targetRookMovesBitmask,
                                targetSquare,
                                attackingPiece.square,
                                freeSquares))
                            {
                                return true;
                            }
                        }
                        break;
                    default:
                        // TODO: Implement me
                        break;
                }
            }
            return false;
        }

        private static bool IsKnightDelta(MoveDelta moveDelta)
        {
            int absDeltaRow = Abs(moveDelta.DeltaRow);
            if (absDeltaRow == 0 || absDeltaRow > 2)
            {
                return false;
            }
            return absDeltaRow + Abs(moveDelta.DeltaColumn) == 3;
        }

        private static bool IsBishopDelta(MoveDelta moveDelta)
        {
            return (moveDelta.DeltaRow == moveDelta.DeltaColumn) ||
                (moveDelta.DeltaRow == -moveDelta.DeltaColumn);
        }

        private static bool IsBishopAttack(
            Bitmask64 targetBishopMovesBitmask,
            BoardSquare targetSquare,
            BoardSquare attackerSquare,
            long freeSquares)
        {
            if (!IsBishopDelta(new MoveDelta(targetSquare, attackerSquare)))
            {
                return false;
            }
            return IsAllSquaresEmptyAlongBishopAttack(
                targetBishopMovesBitmask,
                targetSquare,
                attackerSquare,
                freeSquares);
        }

        private static bool IsRookAttack(
            Bitmask64 targetRookMovesBitmask,
            BoardSquare targetSquare,
            BoardSquare attackerSquare,
            long freeSquares)
        {
            if (targetSquare.Row == attackerSquare.Row)
            {
                return IsAllSquaresEmptyAlongHorizontalAttack(
                    targetRookMovesBitmask,
                    targetSquare.Column,
                    attackerSquare.Column,
                    freeSquares);
            }
            else if (targetSquare.Column == attackerSquare.Column)
            {
                return IsAllSquaresEmptyAlongVerticalAttack(
                    targetRookMovesBitmask,
                    targetSquare.Row,
                    attackerSquare.Row,
                    freeSquares);
            }
            return false;
        }

        private static bool IsAllSquaresEmptyAlongBishopAttack(
            Bitmask64 targetBishopMovesBitmask,
            BoardSquare targetSquare,
            BoardSquare attackerSquare,
            long freeSquares)
        {
            long rayAttack = targetBishopMovesBitmask.Value &
                GetBitmaskInBetweenRows(targetSquare.Row, attackerSquare.Row).Value &
                GetBitmaskInBetweenColumns(targetSquare.Column, attackerSquare.Column).Value;
            return rayAttack == (rayAttack & freeSquares);
        }

        private static bool IsAllSquaresEmptyAlongHorizontalAttack(
            Bitmask64 targetRookMovesBitmask,
            int targetColumn,
            int attackerColumn,
            long freeSquares)
        {
            long rayAttack = targetRookMovesBitmask.Value &
                GetBitmaskInBetweenColumns(targetColumn, attackerColumn).Value;
            return rayAttack == (rayAttack & freeSquares);
        }

        private static bool IsAllSquaresEmptyAlongVerticalAttack(
            Bitmask64 targetRookMovesBitmask,
            int targetRow,
            int attackerRow,
            long freeSquares)
        {
            long rayAttack = targetRookMovesBitmask.Value &
                GetBitmaskInBetweenRows(targetRow, attackerRow).Value;
            return rayAttack == (rayAttack & freeSquares);
        }

        private static Bitmask64 GetBitmaskInBetweenRows(int row1, int row2)
        {
            return bimasksInBetweenRows[row1 * Board.RowCount + row2];
        }

        private static Bitmask64 GetBitmaskInBetweenColumns(int column1, int column2)
        {
            return bimasksInBetweenColumns[column1 * Board.ColumnCount + column2];
        }

        private static Bitmask64[] GenerateBimasksInBetweenIndices(Func<int, Bitmask64> bitmaskByIndexFunc)
        {
            var result = new Bitmask64[Board.SquareCount];
            for (int index1 = 0; index1 < 8; ++index1)
            {
                for (int index2 = 0; index2 < 8; ++index2)
                {
                    var index = index1 * Board.ColumnCount + index2;
                    result[index] = GenerateBimaskInBetweenIndices(index1, index2, bitmaskByIndexFunc);
                }
            }
            return result;
        }

        private static Bitmask64 GenerateBimaskInBetweenIndices(
            int index1,
            int index2,
            Func<int, Bitmask64> bitmaskByIndexFunc)
        {
            var result = new Bitmask64();
            int minIndex = Min(index1, index2);
            int maxIndex = Max(index1, index2);
            for (int index = minIndex + 1; index < maxIndex; ++index)
            {
                result = new Bitmask64(result.Value | bitmaskByIndexFunc(index).Value);
            }
            return result;
        }
    }
}
