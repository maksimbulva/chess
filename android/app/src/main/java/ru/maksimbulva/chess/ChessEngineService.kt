package ru.maksimbulva.chess

import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import ru.maksimbulva.chess.chesslib.ChesslibWrapper
import ru.maksimbulva.chess.core.engine.Engine
import ru.maksimbulva.chess.core.engine.position.Position

class ChessEngineService {

    private val engine = Engine()
    private val chesslibWrapper = ChesslibWrapper()

    private val _currentPosition: Subject<Position>
            = BehaviorSubject.createDefault(engine.currentPosition)

    val currentPosition: Flowable<Position>
        get() = _currentPosition.toFlowable(BackpressureStrategy.LATEST)

    fun playBestMoveAsync() {
        val bestVariation = chesslibWrapper.findBestVariation(onlyFirstMove = true)
        with (bestVariation.moves.first()) {
            engine.playMove(this)
            chesslibWrapper.playMove(this)
        }
        _currentPosition.onNext(engine.currentPosition)
    }
}
