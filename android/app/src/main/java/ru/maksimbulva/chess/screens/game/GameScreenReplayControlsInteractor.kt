package ru.maksimbulva.chess.screens.game

import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.BehaviorSubject
import ru.maksimbulva.ui.replay.items.ReplayGameControlItem

class GameScreenReplayControlsInteractor(
    private val gameInteractor: GameScreenInteractor
) {
    private val _replayControls: BehaviorSubject<List<ReplayGameControlItem>> =
        BehaviorSubject.createDefault(emptyList())

    val replayControls: Flowable<List<ReplayGameControlItem>>
        get() = _replayControls.toFlowable(BackpressureStrategy.LATEST)

    val currentReplayControls: List<ReplayGameControlItem>
        get() = _replayControls.value!!

    init {
        resetControls()
    }

    fun resetControls() {
        _replayControls.onNext(
            listOf(
                ReplayGameControlItem.Pause
            )
        )
    }

    fun onReplayControlButtonClicked(item: ReplayGameControlItem) {
        when (item) {
            ReplayGameControlItem.Pause -> gameInteractor.stop()
            ReplayGameControlItem.Play -> gameInteractor.start()
        }
    }

    fun onEngineStateUpdate(engineState: GameScreenEngineState) {
        _replayControls.onNext(getReplayControlsList(engineState))
    }

    private fun getReplayControlsList(
        engineState: GameScreenEngineState
    ): List<ReplayGameControlItem> {
        return when (engineState) {
            GameScreenEngineState.Stopped -> {
                listOf(
                    ReplayGameControlItem.Play
                )
            }
            GameScreenEngineState.WaitingForUserMove -> {
                emptyList()
            }
            GameScreenEngineState.Running -> {
                listOf(
                    ReplayGameControlItem.Pause
                )
            }
        }
    }
}
