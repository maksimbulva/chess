package ru.maksimbulva.chess.screens.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.maksimbulva.chess.chess.ChessEngineService
import ru.maksimbulva.chess.core.PlayerMap
import ru.maksimbulva.chess.core.engine.Player
import ru.maksimbulva.chess.core.engine.move.DetailedMove
import ru.maksimbulva.chess.core.engine.position.Position
import ru.maksimbulva.ui.person.PersonPanelState
import ru.maksimbulva.ui.replay.items.ReplayGameControlItem

class GameScreenViewModel : ViewModel() {

    data class ViewState(
        val gameState: GameState,
        val position: Position,
        val selectedHistoryMove: DetailedMove?,
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

fun createPersonPanelState(
    chessEngineService: ChessEngineService,
    player: Player
) = PersonPanelState(
    portraitResId = chessEngineService.person(player).portrait,
    nameResId = chessEngineService.person(player).nameResId
)
