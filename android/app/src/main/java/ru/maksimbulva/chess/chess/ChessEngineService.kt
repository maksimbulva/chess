package ru.maksimbulva.chess.chess

import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import ru.maksimbulva.chess.chesslib.ChesslibWrapper
import ru.maksimbulva.chess.core.engine.Engine
import ru.maksimbulva.chess.core.engine.Player
import ru.maksimbulva.chess.core.engine.move.Move
import ru.maksimbulva.chess.core.engine.position.Position
import ru.maksimbulva.chess.person.Person

class ChessEngineService {

    private val engine = Engine()
    private val chesslibWrapper = ChesslibWrapper()

    private val gameAdjudicator = GameAdjudicator(engine)

    private val _currentPosition: Subject<Position>
            = BehaviorSubject.createDefault(engine.currentPosition)

    val currentPosition: Flowable<Position>
        get() = _currentPosition.toFlowable(BackpressureStrategy.LATEST)

    private lateinit var _players: Map<Player, Person>

    val currentPersonToMove: Person
        get() = _players.getValue(engine.currentPosition.playerToMove)

    fun setPlayers(personSideWhite: Person, personSideBlack: Person) {
        _players = mapOf(
            Player.Black to personSideWhite,
            Player.White to personSideBlack
        )
        _players.keys.forEach { configurePlayer(it) }
    }

    fun playBestMoveAsync(): Completable {
        return Single.fromCallable {
            chesslibWrapper.findBestVariation(onlyFirstMove = true)
        }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess { bestVariation ->
                if (bestVariation.moves.isEmpty()) {
                    // TODO: set game result
                } else {
                    playMove(bestVariation.moves.first())
                    _currentPosition.onNext(engine.currentPosition)
                }
            }
            .ignoreElement()
    }

    fun playMove(move: Move) {
        engine.playMove(move)
        chesslibWrapper.playMove(move)
    }

    fun adjudicateGame(): GameAdjudicationResult {
        return gameAdjudicator.checkCurrentPosition(
            isComputer = currentPersonToMove is Person.Computer
        )
    }

    private fun configurePlayer(player: Player) {
        val person = _players.getValue(player)
        if (person !is Person.Computer) {
            return
        }
        with (chesslibWrapper.getPlayer(player)) {
            setPlayerEvaluationsLimit(person.evaluationsLimit)
            setDegreeOfRandomness(person.degreeOfRandomness)
        }
    }
}
