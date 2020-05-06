package ru.maksimbulva.chess.screens.game

import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.SerialDisposable
import io.reactivex.subjects.BehaviorSubject
import ru.maksimbulva.chess.chess.ChessEngineService
import ru.maksimbulva.chess.chess.GameAdjudicationResult
import ru.maksimbulva.chess.core.engine.move.DetailedMove
import ru.maksimbulva.chess.person.Person
import java.util.concurrent.TimeUnit

class GameScreenInteractor(private val chessEngineService: ChessEngineService) {

    private val engineTaskDisposable = SerialDisposable()

    private val _engineState: BehaviorSubject<GameScreenEngineState> =
        BehaviorSubject.createDefault(GameScreenEngineState.Stopped)

    val engineState: Flowable<GameScreenEngineState>
        get() = _engineState.toFlowable(BackpressureStrategy.LATEST)

    val currentEngineState: GameScreenEngineState
        get() = _engineState.value!!

    fun onPositionChanged(adjudicationResult: GameAdjudicationResult) {
        when (adjudicationResult) {
            is GameAdjudicationResult.PlayOn -> {
                when (chessEngineService.currentPersonToMove) {
                    is Person.Computer -> onComputerToMove()
                    is Person.Human -> onHumanToMove()
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
        if (currentEngineState == GameScreenEngineState.Stopped) {
            _engineState.onNext(GameScreenEngineState.WaitingForUserMove)
            onPositionChanged(chessEngineService.adjudicateGame())
        }
    }

    fun stop() {
        _engineState.onNext(GameScreenEngineState.Stopped)
        engineTaskDisposable.set(null)
    }

    fun goBackToMove(detailedMove: DetailedMove, moveHistory: List<DetailedMove>) {
        engineTaskDisposable.set(
            waitUnitlEngineReady().subscribe {
                _engineState.onNext(GameScreenEngineState.Stopped)
                val moveIndex = moveHistory.indexOf(detailedMove)
                if (moveIndex in moveHistory.indices) {
                    chessEngineService.setMoveHistory(moveHistory.subList(0, moveIndex + 1))
                }
            }
        )
    }

    private fun waitUnitlEngineReady() : Completable {
        return Flowable.interval(0, 500, TimeUnit.MILLISECONDS)
            .filter { !chessEngineService.isEngineBusy }
            .take(1)
            .observeOn(AndroidSchedulers.mainThread())
            .ignoreElements()
    }

    private fun onComputerToMove() {
        if (currentEngineState == GameScreenEngineState.WaitingForUserMove) {
            _engineState.onNext(GameScreenEngineState.Running)
            engineTaskDisposable.replace(
                chessEngineService.playBestMoveAsync(
                    doOnEngineSearchFinished = this::onEngineSearchFinished
                ).subscribe()
            )
        }
    }

    private fun onHumanToMove() {
        engineTaskDisposable.set(null)
        _engineState.onNext(GameScreenEngineState.WaitingForUserMove)
    }

    private fun onEngineSearchFinished() {
        if (currentEngineState == GameScreenEngineState.Running) {
            _engineState.onNext(GameScreenEngineState.WaitingForUserMove)
        }
    }
}
