using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using Android.App;
using Android.Content;
using Android.OS;
using Android.Runtime;
using Android.Util;
using Android.Views;
using Android.Widget;
using UI.Items;

namespace UI.Internal.Chessboard
{
    internal class ChessboardSquareView : SquareFrameLayout
    {
        private ImageView background;

        public ChessboardSquareView(Context context, IAttributeSet attrs) :
            base(context, attrs)
        {
            Initialize();
        }

        public ChessboardSquareView(Context context, IAttributeSet attrs, int defStyle) :
            base(context, attrs, defStyle)
        {
            Initialize();
        }

        public void Bind(ChessboardItem item)
        {
            background.SetBackgroundResource(GetBackgroundImageResource(item.color));
        }

        private void Initialize()
        {
            Inflate(Context, Resource.Layout.chessboard_square_view, this);

            background = FindViewById<ImageView>(Resource.Id.chessboard_square_background);
        }

        private int GetBackgroundImageResource(ChessboardItem.CellColor color)
        {
            return color switch
            {
                ChessboardItem.CellColor.Dark => Resource.Drawable.chessboard_square_fritz_dark,
                ChessboardItem.CellColor.Light => Resource.Drawable.chessboard_square_fritz_light,
                _ => 0,
            };
        }
    }
}
