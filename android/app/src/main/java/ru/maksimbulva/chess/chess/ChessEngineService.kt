package ru.maksimbulva.chess.chess

import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import ru.maksimbulva.chess.chesslib.ChesslibWrapper
import ru.maksimbulva.chess.core.PlayerMap
import ru.maksimbulva.chess.core.engine.Engine
import ru.maksimbulva.chess.core.engine.Player
import ru.maksimbulva.chess.core.engine.Variation
import ru.maksimbulva.chess.core.engine.move.Move
import ru.maksimbulva.chess.core.engine.position.Position
import ru.maksimbulva.chess.person.Person

class ChessEngineService {

    private val engine = Engine()
    private val chesslibWrapper = ChesslibWrapper()

    private val gameAdjudicator = GameAdjudicator(engine)

    private val _position: BehaviorSubject<Position>
        = BehaviorSubject.createDefault(engine.currentPosition)

    val position: Flowable<Position>
        get() = _position.toFlowable(BackpressureStrategy.LATEST)

    val currentPosition: Position
        get() = _position.value!!

    private val _bestVariation: BehaviorSubject<PlayerMap<Variation?>>
        = BehaviorSubject.createDefault(PlayerMap<Variation?>(null, null))

    val bestVariation: Flowable<PlayerMap<Variation?>>
        get() = _bestVariation.toFlowable(BackpressureStrategy.LATEST)

    private var _players = PlayerMap<Person>(
        blackPlayerValue = Person.Human(),
        whitePlayerValue = Person.Human()
    )

    val currentPersonToMove: Person
        get() = _players.get(engine.currentPosition.playerToMove)

    fun setPlayers(persons: PlayerMap<Person>) {
        _players = persons
        Player.values().forEach(this::configurePlayer)
    }

    fun playBestMoveAsync(): Completable {
        return Single.fromCallable {
            chesslibWrapper.findBestVariation(onlyFirstMove = true)
        }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess { bestVariation ->
                _bestVariation.onNext(
                    _bestVariation.value!!.copyWith(
                        player = engine.currentPosition.playerToMove,
                        value = bestVariation
                    )
                )
                if (bestVariation.moves.isEmpty()) {
                    // TODO: set game result
                } else {
                    playMove(bestVariation.moves.first())
                }
            }
            .ignoreElement()
    }

    fun playMove(move: Move) {
        engine.playMove(move)
        chesslibWrapper.playMove(move)
        publishPositionUpdate()
    }

    fun adjudicateGame(): GameAdjudicationResult {
        return gameAdjudicator.checkCurrentPosition(
            isComputer = currentPersonToMove is Person.Computer
        )
    }

    private fun publishPositionUpdate() {
        _position.onNext(engine.currentPosition)
    }

    private fun configurePlayer(player: Player) {
        val person = _players.get(player)
        if (person !is Person.Computer) {
            return
        }
        with (chesslibWrapper.getPlayer(player)) {
            setPlayerEvaluationsLimit(person.evaluationsLimit)
            setDegreeOfRandomness(person.degreeOfRandomness)
        }
    }
}
