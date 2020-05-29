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
    public class PersonPanelView : FrameLayout
    {
        private TextView personNameView;

        public PersonPanelView(Context context, IAttributeSet attrs) :
            base(context, attrs)
        {
            Initialize();
        }

        public PersonPanelView(Context context, IAttributeSet attrs, int defStyle) :
            base(context, attrs, defStyle)
        {
            Initialize();
        }

        public void SetPersonName(string value)
        {
            personNameView.Text = value;
        }

        private void Initialize()
        {
            Inflate(Context, Resource.Layout.person_panel_view, this);

            personNameView = FindViewById<TextView>(Resource.Id.person_name);
        }
    }
}