package ru.maksimbulva.chess.screens.game

import io.reactivex.disposables.SerialDisposable
import ru.maksimbulva.chess.chess.ChessEngineService
import ru.maksimbulva.chess.chess.GameAdjudicationResult
import ru.maksimbulva.chess.person.Person

class GameScreenInteractor(private val chessEngineService: ChessEngineService) {

    private val playBestMoveDisposable = SerialDisposable()

    fun onPositionChanged(adjudicationResult: GameAdjudicationResult) {
        when (adjudicationResult) {
            is GameAdjudicationResult.PlayOn -> {
                if (chessEngineService.currentPersonToMove is Person.Computer) {
                    playBestMoveDisposable.replace(
                        chessEngineService.playBestMoveAsync().subscribe()
                    )
                }
            }
            is GameAdjudicationResult.PlayMove -> {
                chessEngineService.playMove(adjudicationResult.move)
            }
        }
    }

    fun stop() {
        playBestMoveDisposable.set(null)
    }
}
