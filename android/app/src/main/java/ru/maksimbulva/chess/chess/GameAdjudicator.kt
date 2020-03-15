package ru.maksimbulva.chess.chess

import ru.maksimbulva.chess.chess.GameAdjudicationResult.Draw.DrawReason
import ru.maksimbulva.chess.core.engine.move.Move
import ru.maksimbulva.chess.core.engine.otherPlayer
import ru.maksimbulva.chess.core.engine.position.Position

object GameAdjudicator {

    fun checkCurrentPosition(
        currentPosition: Position,
        legalMoves: List<Move>,
        isComputer: Boolean
    ): GameAdjudicationResult {
        val playerToMove = currentPosition.playerToMove
        return when {
            legalMoves.isEmpty() -> {
                if (currentPosition.isInCheck) {
                    GameAdjudicationResult.Win(playerToMove.otherPlayer())
                } else {
                    GameAdjudicationResult.Draw(DrawReason.Stalemate)
                }
            }
            isComputer && legalMoves.size == 1 -> {
                GameAdjudicationResult.PlayMove(legalMoves.first())
            }
            isComputer && currentPosition.halfMoveClock >= 100 -> {
                GameAdjudicationResult.Draw(DrawReason.FiftyMovesRule)
            }
            else -> GameAdjudicationResult.PlayOn
        }
    }
}
