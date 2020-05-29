using System;
using System.Reactive.Disposables;

namespace ChessApp.Mvp
{
    internal abstract class BasePresenter<V> where V : class, IView
    {
        protected V attachedView;

        protected readonly CompositeDisposable disposables = new CompositeDisposable();

        public virtual void OnCreate()
        {
        }

        public virtual void OnAttachedView(V view)
        {
            attachedView = view;
        }

        public virtual void OnStart() {
        }

        public virtual void OnStop() {
        }

        public virtual void OnDetachedView() {
            attachedView = null;
            disposables.Dispose();
            disposables.Clear();
        }

        public virtual void OnDestroyed() {
        }

        protected void AddSubscription(IDisposable disposable) {
            disposables.Add(disposable);
        }
    }
}
