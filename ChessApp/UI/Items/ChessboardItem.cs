using ChessEngine;
using ChessEngine.Board;

namespace UI.Items
{
    public class ChessboardItem : Java.Lang.Object
    {
        public readonly Square cell;
        public readonly Player? player;
        public readonly Piece? piece;
        public readonly CellColor color;

        public ChessboardItem(Square cell, Player? player, Piece? piece, CellColor color)
        {
            this.cell = cell;
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
