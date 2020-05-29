using Android.OS;
using Android.Views;

namespace ChessApp.Mvp
{
    internal abstract class BaseFragment<P, V> : AndroidX.Fragment.App.Fragment
        where P : BasePresenter<V>
        where V : class, IView
    {
        protected readonly P presenter;

        protected abstract V AttachedView { get; }

        public BaseFragment(int contentLayoutId)
            : base(contentLayoutId)
        {
            presenter = CreatePresenter();
        }

        public override void OnCreate(Bundle savedInstanceState)
        {
            base.OnCreate(savedInstanceState);

            presenter.OnCreate();
        }

        public override void OnViewCreated(View view, Bundle savedInstanceState)
        {
            base.OnViewCreated(view, savedInstanceState);

            OnAttachingView(view);
            presenter.OnAttachedView(this as V);
        }

        public override void OnStart()
        {
            base.OnStart();

            presenter.OnStart();
        }


        public override void OnStop()
        {
            base.OnStop();

            presenter.OnStop();
        }

        public override void OnDestroyView()
        {
            base.OnDestroyView();

            presenter.OnDetachedView();
        }

        public override void OnDestroy()
        {
            base.OnDestroy();

            presenter.OnDestroyed();
        }

        protected abstract P CreatePresenter();

        protected abstract void OnAttachingView(View view);
    }
}
