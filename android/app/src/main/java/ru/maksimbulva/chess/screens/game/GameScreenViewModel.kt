package ru.maksimbulva.chess.screens.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.maksimbulva.chess.core.engine.Player
import ru.maksimbulva.chess.core.engine.Variation
import ru.maksimbulva.chess.core.engine.position.Position

class GameScreenViewModel : ViewModel() {
    private val _boardState = MutableLiveData<GameScreenBoardState>()
    val boardState: LiveData<GameScreenBoardState>
        get() = _boardState

    private val _personsState = MutableLiveData<Map<Player, GameScreenPersonState>>()
    val personsState: LiveData<Map<Player, GameScreenPersonState>>
        get() = _personsState

    fun setPosition(position: Position) {
        _boardState.value = GameScreenBoardState(position)
    }

    fun updateBestVariation(player: Player, bestVariation: Variation) {
        _personsState.value =
            _personsState.value?.toMutableMap()?.also {
                it[player] = it.getValue(player).copy(bestVariation = bestVariation)
            }
    }

    init {
        _personsState.value = Player.values().map { it to GameScreenPersonState() }.toMap()
    }
}
