package ru.maksimbulva.chess.screens.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.maksimbulva.chess.chess.GameAdjudicationResult
import ru.maksimbulva.chess.core.PlayerMap
import ru.maksimbulva.chess.core.engine.Player
import ru.maksimbulva.chess.core.engine.move.DetailedMove
import ru.maksimbulva.chess.core.engine.position.Position
import ru.maksimbulva.ui.person.PersonPanelState
import ru.maksimbulva.ui.replay.items.ReplayGameControlItem

class GameScreenViewModel : ViewModel() {

    data class ViewState(
        val position: Position,
        val moveHistory: List<DetailedMove>,
        val adjudicationResult: GameAdjudicationResult,
        val playerOnTop: Player,
        val playersState: PlayerMap<PersonPanelState>,
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
