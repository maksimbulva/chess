package ru.maksimbulva.chess.chess

import ru.maksimbulva.chess.core.engine.Engine
import ru.maksimbulva.chess.core.engine.otherPlayer
import ru.maksimbulva.chess.chess.GameAdjudicationResult.Draw.DrawReason

class GameAdjudicator(private val engine: Engine) {

    fun checkCurrentPosition(isComputer: Boolean): GameAdjudicationResult {
        val playerToMove = engine.currentPosition.playerToMove
        return when {
            engine.legalMoves.isEmpty() -> {
                if (engine.currentPosition.isInCheck) {
                    GameAdjudicationResult.Win(playerToMove.otherPlayer())
                } else {
                    GameAdjudicationResult.Draw(DrawReason.Stalemate)
                }
            }
            isComputer && engine.legalMoves.size == 1 -> {
                GameAdjudicationResult.PlayMove(engine.legalMoves.first())
            }
            else -> GameAdjudicationResult.PlayOn
        }
    }
}
