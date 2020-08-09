package ru.maksimbulva.chess.screens.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.maksimbulva.chess.chess.ChessEngineState
import ru.maksimbulva.chess.core.engine.Player
import ru.maksimbulva.chess.core.engine.move.DetailedMove
import ru.maksimbulva.ui.replay.items.ReplayGameControlItem

class GameScreenViewModel : ViewModel() {

    data class ViewState(
        val gameState: GameState,
        val chessEngineState: ChessEngineState,
        val selectedHistoryMove: DetailedMove?,
        val playerOnTop: Player,
        val moveListCollapsed: Boolean,
        val replayControlItems: List<ReplayGameControlItem>
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
