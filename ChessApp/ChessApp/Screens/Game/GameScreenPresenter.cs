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
using ChessApp.Mvp;

namespace ChessApp.Screens.Game
{
    internal class GameScreenPresenter : BasePresenter<IGameScreenView>
    {
        public override void OnAttachedView(IGameScreenView view)
        {
            base.OnAttachedView(view);

            // TODO: Remove hardcoded data
            view.SetPlayerName(Resource.String.person_alice_name, IGameScreenView.PlayerPosition.Top);
            view.SetPlayerName(Resource.String.person_bob_name, IGameScreenView.PlayerPosition.Bottom);
        }
    }
}
