using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using Android.App;
using Android.Content;
using Android.OS;
using Android.Runtime;
using Android.Views;
using Android.Widget;
using UI.Items;

namespace UI.Internal.Chessboard
{
    internal class ChessboardAdapter
        : BaseAdapter
    {
        private readonly Context _context;

        private readonly List<ChessboardItem> items = new List<ChessboardItem>();

        public ChessboardAdapter(Context context)
        {
            _context = context;
        }

        public override int Count => items.Count;

        public override Java.Lang.Object GetItem(int position) => items[position];

        public override long GetItemId(int position) => 0;

        public override View GetView(int position, View convertView, ViewGroup parent)
        {
            ChessboardSquareView view = convertView as ChessboardSquareView;
            if (view == null)
            {
                view = new ChessboardSquareView(_context, null);
            }
            view.Bind(items[position]);
            return view;
        }

        public void SetItems(IEnumerable<ChessboardItem> items)
        {
            this.items.Clear();
            this.items.AddRange(items);
            NotifyDataSetChanged();
        }
    }
}
