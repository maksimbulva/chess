using Android.Content;
using Android.Util;
using Android.Widget;
using ChessEngine;
using ChessEngine.Board;
using Java.Lang;
using System.Collections.Generic;
using UI.Internal.Chessboard;
using UI.Items;

namespace UI
{
    public class ChessboardView : SquareFrameLayout
    {
        private GridView grid;
        private ChessboardAdapter adapter;

        public ChessboardView(Context context, IAttributeSet attrs) :
            base(context, attrs)
        {
            Initialize();
        }

        public ChessboardView(Context context, IAttributeSet attrs, int defStyle) :
            base(context, attrs, defStyle)
        {
            Initialize();
        }

        public void SetItems(IEnumerable<ChessboardItem> items)
        {
            adapter.SetItems(items);
        }

        private void Initialize()
        {
            Inflate(Context, Resource.Layout.chessboard_view, this);

            grid = FindViewById<GridView>(Resource.Id.chessboard_grid);

            adapter = new ChessboardAdapter(Context);

            grid.Adapter = adapter;

            var items = new List<ChessboardItem>();

            // TODO: Temporary
            for (int row = 0; row < 8; ++row)
            {
                for (int column = 0; column < 8; ++column)
                {
                    var cellColor = ((row + column) % 2) switch
                    {
                        0 => ChessboardItem.CellColor.Dark,
                        _ => ChessboardItem.CellColor.Light
                    };
                    items.Add(new ChessboardItem(new Square(row, column), null, null, cellColor));
                }
            }

            SetItems(items);
        }
    }
}
