using ChessEngine;
using ChessEngine.Board;

namespace UI.Items
{
    public class ChessboardItem : Java.Lang.Object
    {
        public readonly BoardSquare boardSquare;
        public readonly Player? player;
        public readonly Piece? piece;
        public readonly CellColor color;

        public ChessboardItem(BoardSquare boardSquare, Player? player, Piece? piece, CellColor color)
        {
            this.boardSquare = boardSquare;
            this.player = player;
            this.piece = piece;
            this.color = color;
        }

        public enum CellColor
        {
            Dark,
            Light
        }
    }
}
