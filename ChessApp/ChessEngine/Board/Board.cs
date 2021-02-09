using ChessEngine.Bitmask;
using ChessEngine.Internal;
using System.Collections.Generic;
using static ChessEngine.Move.Move;

namespace ChessEngine.Board
{
    public sealed class Board
    {
        public const int RowCount = 8;
        public const int ColumnCount = 8;
        public const int SquareCount = RowCount * ColumnCount;

        private readonly PieceTable _pieceTable;

        private Bitmask64 _occupiedSquares;
        internal Bitmask64 OccupiedSquares => _occupiedSquares;

        public Board(List<PieceOnBoard> pieces)
        {
            _pieceTable = new PieceTable(pieces);
            _occupiedSquares = CalculateOccupiedSquaresBitmask();
        }

        public bool IsEmpty(int row, int column)
        {
            return !_occupiedSquares.IsBitSet(ToSquareIndex(row, column));
        }

        public bool IsEmpty(BoardSquare square)
        {
            return !_occupiedSquares.IsBitSet(square.IntValue);
        }

        public IEnumerable<PieceOnBoard> GetPieces(Player player) => _pieceTable.GetPieces(player);

        public BoardSquare GetKingSquare(Player player) => _pieceTable.GetKingSquare(player);

        public PieceOnBoard GetPieceAt(BoardSquare square)
        {
            return _pieceTable.GetPieceAt(square.IntValue);
        }

        internal void PlayMove(Move.Move legalMove, Player player)
        {
            if (legalMove.IsCapture)
            {
                var capturedPieceSquareIndex = GetCapturedPieceSquare(legalMove).IntValue;
                _pieceTable.RemovePieceAt(capturedPieceSquareIndex);
                _occupiedSquares.UnsetBit(capturedPieceSquareIndex);
            }

            MovePiece(legalMove.OriginSquare, legalMove.DestSquare);

            if (legalMove.IsPromotion)
            {
                _pieceTable.UpdatePiece(legalMove.DestSquare.IntValue, legalMove.GetPromoteToPiece());
            }
            else if (legalMove.IsShortCastle)
            {
                PlayMove(GetShortCastleRookMove(player), player);
            }
            else if (legalMove.IsLongCastle)
            {
                PlayMove(GetLongCastleRookMove(player), player);
            }
        }

        internal void UndoMove(Move.Move move, Player player)
        {
            MovePiece(move.DestSquare, move.OriginSquare);

            if (move.IsCapture)
            {
                var capturedPieceSquare = GetCapturedPieceSquare(move);
                var pieceToRessurect = new PieceOnBoard(
                    Utils.GetOtherPlayer(player),
                    move.GetCapturedPiece(),
                    capturedPieceSquare);
                _pieceTable.InsertPiece(pieceToRessurect);
                _occupiedSquares.SetBit(capturedPieceSquare.IntValue);
            }

            if (move.IsPromotion)
            {
                _pieceTable.UpdatePiece(move.OriginSquare.IntValue, Piece.Pawn);
            }
            else if (move.IsShortCastle)
            {
                UndoMove(GetShortCastleRookMove(player), player);
            }
            else if (move.IsLongCastle)
            {
                UndoMove(GetLongCastleRookMove(player), player);
            }
        }

        private void MovePiece(BoardSquare originSquare, BoardSquare destSquare)
        {
            _pieceTable.MovePiece(originSquare.IntValue, destSquare.IntValue);
            _occupiedSquares.UnsetBit(originSquare.IntValue);
            _occupiedSquares.SetBit(destSquare.IntValue);
        }

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

        private static int ToSquareIndex(int row, int column) => row << 3 | column;

        private static BoardSquare GetCapturedPieceSquare(Move.Move captureMove)
        {
            if (captureMove.IsEnPassantCapture)
            {
                return new BoardSquare(captureMove.OriginSquare.Row, captureMove.DestSquare.Column);
            }
            else
            {
                return captureMove.DestSquare;
            }
        }
    }
}
