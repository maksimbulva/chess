package ru.maksimbulva.chess.mvp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import ru.maksimbulva.chess.ActionBarPresenter
import ru.maksimbulva.chess.MainActivity

abstract class BaseFragment<P, V, VM, A>(
    @LayoutRes contentLayoutId: Int
) : Fragment(contentLayoutId) where VM : ViewModel, P : BasePresenter<V, VM, A> {

    protected val presenter: P = createPresenter()

    protected lateinit var viewModel: VM

    protected abstract val view: V

    protected val actionBarPresenter: ActionBarPresenter?
        get() = (activity as? MainActivity)?.actionBarPresenter

    protected abstract fun onViewCreated(view: View)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = obtainViewModel()
        presenter.onCreate(viewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState).also {
            it?.let { onViewCreated(it) }
            presenter.onAttachedView(view)
        }
    }

    override fun onStart() {
        super.onStart()
        presenter.onStart()
    }

    override fun onStop() {
        super.onStop()
        presenter.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDetachedView()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroyed()
    }

    fun publishAction(action: A) {
        presenter.onActionReceived(action)
    }

    protected abstract fun createPresenter(): P

    protected abstract fun obtainViewModel(): VM
}
