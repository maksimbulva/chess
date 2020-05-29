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

namespace UI
{
    public class ReplayGameControlsView : FrameLayout
    {
        public ReplayGameControlsView(Context context, IAttributeSet attrs) :
            base(context, attrs)
        {
            Initialize();
        }

        public ReplayGameControlsView(Context context, IAttributeSet attrs, int defStyle) :
            base(context, attrs, defStyle)
        {
            Initialize();
        }

        private void Initialize()
        {
        }
    }
}