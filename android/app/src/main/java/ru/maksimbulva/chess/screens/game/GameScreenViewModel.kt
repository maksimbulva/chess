package ru.maksimbulva.chess.screens.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.maksimbulva.chess.core.PlayerMap
import ru.maksimbulva.chess.core.engine.Player
import ru.maksimbulva.chess.core.engine.position.Position

class GameScreenViewModel : ViewModel() {

    data class ViewState(
        val position: Position,
        val playerOnTop: Player,
        val playersState: PlayerMap<GameScreenPersonState>
    ) {
        val boardItems = ChessboardItemsGenerator.generateForBoard(position.board, playerOnTop)
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
