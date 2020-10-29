package ru.maksimbulva.chess.screens.game_setup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameSetupScreenViewModel : ViewModel() {

    data class ViewState(
        val shouldStartGame: Boolean
    )

    private val _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState>
        get() = _viewState

    var currentState: ViewState
        get() = _viewState.value!!
        set(value) {
            _viewState.value = value
        }
}
