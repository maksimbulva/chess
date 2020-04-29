package ru.maksimbulva.chess.screens.game

import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.disposables.SerialDisposable
import io.reactivex.subjects.BehaviorSubject
import ru.maksimbulva.chess.chess.ChessEngineService
import ru.maksimbulva.chess.chess.GameAdjudicationResult
import ru.maksimbulva.chess.person.Person

class GameScreenInteractor(private val chessEngineService: ChessEngineService) {

    private val playBestMoveDisposable = SerialDisposable()

    private val _engineState: BehaviorSubject<GameScreenEngineState> =
        BehaviorSubject.createDefault(GameScreenEngineState.Stopped)

    val engineState: Flowable<GameScreenEngineState>
        get() = _engineState.toFlowable(BackpressureStrategy.LATEST)

    fun onPositionChanged(adjudicationResult: GameAdjudicationResult) {
        when (adjudicationResult) {
            is GameAdjudicationResult.PlayOn -> {
                if (chessEngineService.currentPersonToMove is Person.Computer) {
                    playBestMoveDisposable.replace(
                        chessEngineService.playBestMoveAsync().subscribe()
                    )
                    _engineState.onNext(GameScreenEngineState.Running)
                } else {
                    _engineState.onNext(GameScreenEngineState.WaitingForUserMove)
                }
            }
            is GameAdjudicationResult.PlayMove -> {
                chessEngineService.playMove(adjudicationResult.move)
            }
            else -> {
                _engineState.onNext(GameScreenEngineState.Stopped)
            }
        }
    }

    fun start() {
        onPositionChanged(chessEngineService.adjudicateGame())
    }

    fun stop() {
        playBestMoveDisposable.set(null)
        _engineState.onNext(GameScreenEngineState.Stopped)
    }
}
