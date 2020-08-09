package ru.maksimbulva.chess.screens.game

import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.BehaviorSubject
import ru.maksimbulva.chess.core.engine.Engine
import ru.maksimbulva.chess.core.engine.move.DetailedMove
import ru.maksimbulva.chess.core.engine.move.Move
import ru.maksimbulva.chess.core.engine.position.Position

class AnalysisModeUseCase {

    private val engine = Engine()

    private val _analysisPosition: BehaviorSubject<Position> =
        BehaviorSubject.createDefault(engine.currentPosition)

    val analysisPosition: Flowable<Position>
        get() = _analysisPosition.toFlowable(BackpressureStrategy.LATEST)

    var currentMove: DetailedMove? = null
        private set

    fun goBackToMove(moveToDisplay: DetailedMove, moveHistory: List<DetailedMove>) {
        engine.resetToInitialPosition()
        moveHistory.takeWhile { currentMove -> currentMove != moveToDisplay }
            .forEach { engine.playMove(Move(it)) }
        with (Move(moveToDisplay)) {
            if (this in engine.legalMoves) {
                engine.playMove(this)
            }
        }
        currentMove = moveToDisplay
        _analysisPosition.onNext(engine.currentPosition)
    }
}
