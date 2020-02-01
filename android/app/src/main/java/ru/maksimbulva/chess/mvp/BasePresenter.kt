package ru.maksimbulva.chess.mvp

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BasePresenter<V, VM> where VM : ViewModel {

    protected var attachedView: V? = null
    protected lateinit var viewModel: VM

    protected val disposables = CompositeDisposable()

    open fun onCreate(viewModel: VM) {
        this.viewModel = viewModel
    }

    open fun onAttachedView(view: V) {
        attachedView = view
    }

    open fun onStart() {
    }

    open fun onStop() {
    }

    open fun onDetachedView() {
        attachedView = null
        disposables.dispose()
        disposables.clear()
    }

    open fun onDestroyed() {
    }

    protected fun addSubscription(disposable: Disposable) {
        disposables.add(disposable)
    }
}
