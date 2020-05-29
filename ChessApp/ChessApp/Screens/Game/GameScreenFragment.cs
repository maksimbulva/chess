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
using ChessApp.Mvp;
using UI;

namespace ChessApp.Screens.Game
{
    internal class GameScreenFragment : BaseFragment<GameScreenPresenter, IGameScreenView>, IGameScreenView
    {
        protected override IGameScreenView AttachedView => this;

        private PersonPanelView topPersonPanelView;

        private PersonPanelView bottomPersonPanelView;

        public GameScreenFragment()
            : base(Resource.Layout.fragment_game_screen)
        {
        }

        protected override GameScreenPresenter CreatePresenter()
        {
            return new GameScreenPresenter();
        }

        public void SetPlayerName(int playerNameResId, IGameScreenView.PlayerPosition playerPosition)
        {
            GetPersonPanelView(playerPosition).SetPersonName(Context.GetString(playerNameResId));
        }

        protected override void OnAttachingView(View view)
        {
            topPersonPanelView = view.FindViewById<PersonPanelView>(Resource.Id.top_person_panel);
            bottomPersonPanelView = view.FindViewById<PersonPanelView>(Resource.Id.bottom_person_panel);
        }

        private PersonPanelView GetPersonPanelView(IGameScreenView.PlayerPosition playerPosition)
        {
            return playerPosition switch
            {
                IGameScreenView.PlayerPosition.Top => topPersonPanelView,
                IGameScreenView.PlayerPosition.Bottom => bottomPersonPanelView,
                _ => throw new NotImplementedException()
            };
        }
    }
}
