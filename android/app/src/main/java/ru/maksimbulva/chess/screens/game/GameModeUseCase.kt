package ru.maksimbulva.chess.screens.game

import io.reactivex.disposables.SerialDisposable
import ru.maksimbulva.chess.chess.ChessEngineService
import ru.maksimbulva.chess.chess.ChessEngineState
import ru.maksimbulva.chess.person.Person

class GameModeUseCase(
    private val chessEngineService: ChessEngineService
) {
    private val engineStateSubscription = SerialDisposable()

    fun start() {
        engineStateSubscription.set(
            chessEngineService.engineState.subscribe { state ->
                if (!chessEngineService.hasCommands && getPersonToMove(state) is Person.Computer) {
                    val bestMove = state.bestMove
                    if (bestMove != null) {
                        chessEngineService.playMove(bestMove)
                    } else {
                        chessEngineService.findBestVariation()
                    }
                }
            }
        )
    }

    fun stop() {
        engineStateSubscription.set(null)
    }

    companion object {
        private fun getPersonToMove(state: ChessEngineState): Person {
            return state.players.get(state.position.playerToMove)
        }
    }
}
