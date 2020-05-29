using System;
using Android.Content;
using Android.Util;
using Android.Widget;

namespace UI
{
    public class SquareFrameLayout : FrameLayout
    {
        public SquareFrameLayout(Context context, IAttributeSet attrs) :
            base(context, attrs)
        {
        }

        public SquareFrameLayout(Context context, IAttributeSet attrs, int defStyle) :
            base(context, attrs, defStyle)
        {
        }

        protected override void OnMeasure(int widthMeasureSpec, int heightMeasureSpec)
        {
            base.OnMeasure(widthMeasureSpec, heightMeasureSpec);
            var minSize = Math.Min(MeasuredWidth, MeasuredHeight);
            SetMeasuredDimension(minSize, minSize);
        }
    }
}
