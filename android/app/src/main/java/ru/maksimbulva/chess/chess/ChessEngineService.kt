package ru.maksimbulva.chess.chess

import io.reactivex.*
import io.reactivex.subjects.BehaviorSubject
import ru.maksimbulva.chess.chess.command.ChessEngineCommand
import ru.maksimbulva.chess.chess.command.FindBestVariationCommand
import ru.maksimbulva.chess.chess.command.PlayMoveCommand
import ru.maksimbulva.chess.chess.command.SetPlayersCommand
import ru.maksimbulva.chess.chesslib.ChesslibWrapper
import ru.maksimbulva.chess.core.PlayerMap
import ru.maksimbulva.chess.core.engine.Engine
import ru.maksimbulva.chess.core.engine.Player
import ru.maksimbulva.chess.core.engine.Variation
import ru.maksimbulva.chess.core.engine.move.DetailedMove
import ru.maksimbulva.chess.core.engine.move.Move
import ru.maksimbulva.chess.person.Person
import java.util.ArrayDeque

class ChessEngineService(
    defaultPerson: Person
) {
    private val engine = Engine()
    private val chesslibWrapper = ChesslibWrapper()

    private var isRunning = false

    private val commandQueue = ArrayDeque<ChessEngineCommand>()
    private var currentCommand: ChessEngineCommand? = null

    val hasCommands: Boolean
        get() = currentCommand != null || commandQueue.isNotEmpty()

    private var _players = PlayerMap(
        blackPlayerValue = defaultPerson,
        whitePlayerValue = defaultPerson
    )

    var players: PlayerMap<Person>
        get() = _players
        set(value) {
            commandQueue.addLast(SetPlayersCommand(value, chesslibWrapper))
            processWithCommandExecution()
        }

    val moveHistory: List<DetailedMove>
        get() = engine.moveHistory

    val isEngineBusy: Boolean
        get() = chesslibWrapper.isBusy()

    private var bestVariation: Variation? = null

    private val _engineState: BehaviorSubject<ChessEngineState> = BehaviorSubject.createDefault(
        createActualChessEngineState()
    )

    val engineState: Flowable<ChessEngineState>
        get() = _engineState.toFlowable(BackpressureStrategy.LATEST)

    val currentState: ChessEngineState
        get() = _engineState.value!!

    val currentPersonToMove: Person
        get() = _players.get(engine.currentPosition.playerToMove)

    fun start() {
        isRunning = true
        processWithCommandExecution()
    }

    fun stop() {
        currentCommand?.let {
            it.abort()
            commandQueue.addFirst(it)
        }
        isRunning = false
    }

    fun findBestVariation() {
        commandQueue.addLast(FindBestVariationCommand(engine, chesslibWrapper))
        processWithCommandExecution()
    }

    fun playMove(move: Move) {
        commandQueue.addLast(PlayMoveCommand(move, engine, chesslibWrapper))
        processWithCommandExecution()
    }

    fun person(player: Player): Person {
        return _players.get(player)
    }

    fun setMoveHistory(moveHistory: List<DetailedMove>) {
//        engine.resetToInitialPosition()
//        chesslibWrapper.resetGame()
//        moveHistory.forEach {
//            playMove(Move(it), shouldPublishUpdate = false)
//        }
//        publishPositionUpdate()
    }

    fun adjudicateGame(): GameAdjudicationResult {
        return GameAdjudicator.checkCurrentPosition(
            engine.currentPosition,
            engine.legalMoves,
            isComputer = currentPersonToMove is Person.Computer
        )
    }

    private fun processWithCommandExecution() {
        if (!isRunning || currentCommand != null) {
            return
        }
        currentCommand = commandQueue.pollFirst()
        with (currentCommand) {
            this?.execute(onComplete = { onCompletedCommand(this) })
        }
    }

    private fun onCompletedCommand(command: ChessEngineCommand) {
        when (command) {
            is SetPlayersCommand -> _players = command.players
            is FindBestVariationCommand -> bestVariation = command.bestVariation
            is PlayMoveCommand -> bestVariation = null
        }
        if (currentCommand === command) {
            currentCommand = null
        }
        _engineState.onNext(createActualChessEngineState())
        processWithCommandExecution()
    }

    private fun createActualChessEngineState(): ChessEngineState {
        return ChessEngineState(
            players = players,
            position = engine.currentPosition,
            moveHistory = engine.moveHistory,
            bestVariation = bestVariation
        )
    }
}
