package ru.maksimbulva.chess.screens.game_setup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.maksimbulva.chess.core.PlayerMap
import ru.maksimbulva.chess.core.engine.Player
import ru.maksimbulva.chess.person.Person

class GameSetupScreenViewModel : ViewModel() {

    sealed class ViewState(val players: PlayerMap<Person?>) {
        class GameLobby(players: PlayerMap<Person?>) : ViewState(players)

        class PersonList(
            players: PlayerMap<Person?>,
            val persons: List<Person>,
            val playerToSelect: Player,
        ) : ViewState(players)
    }

    private val _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState>
        get() = _viewState

    var currentState: ViewState
        get() = _viewState.value!!
        set(value) {
            _viewState.value = value
        }
}
