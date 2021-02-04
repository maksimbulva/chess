﻿using ChessEngine.Bitmask;
using System;
using System.Collections.Generic;
using System.Text;

namespace ChessEngine.Board
{
    public sealed class Board
    {
        public const int RowCount = 8;
        public const int ColumnCount = 8;
        public const int SquareCount = RowCount * ColumnCount;

        private readonly PieceTable _pieceTable;
        private Bitmask64 _occupiedSquares;

        public Board(List<PieceOnBoard> pieces)
        {
            _pieceTable = new PieceTable(pieces);
            _occupiedSquares = CalculateOccupiedSquaresBitmask();
        }

        public bool IsEmpty(int row, int column)
        {
            return !_occupiedSquares.IsBitSet(ToSquareIndex(row, column));
        }

        public PieceOnBoard GetPieceAt(BoardSquare square)
        {
            return _pieceTable.GetPieceAt(square.IntValue);
        }

        private static int ToSquareIndex(int row, int column) => row << 3 | column;

        private Bitmask64 CalculateOccupiedSquaresBitmask()
        {
            Bitmask64 occupiedSquares = new Bitmask64();
            for (int squareIndex = 0; squareIndex < SquareCount; ++squareIndex)
            {
                if (!_pieceTable.IsEmpty(squareIndex))
                {
                    occupiedSquares.SetBit(squareIndex);
                }
            }
            return occupiedSquares;
        }
    }
}
