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
using ChessApp.Screens.Game;

namespace ChessApp.Mvp
{
    internal interface IGameScreenView : IView
    {
        void SetPlayerName(int playerNameResId, PlayerPosition playerPosition);

        enum PlayerPosition
        {
            Top,
            Bottom
        }
    }
}