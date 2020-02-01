package ru.maksimbulva.chess.mvp

import androidx.lifecycle.ViewModel

abstract class BasePresenter<V, VM> where VM : ViewModel {

    protected var attachedView: V? = null
    protected lateinit var viewModel: VM

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
    }

    open fun onDestroyed() {
    }
}
