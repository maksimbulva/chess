package ru.maksimbulva.chess.chess

import ru.maksimbulva.chess.core.engine.Player
import ru.maksimbulva.chess.core.engine.move.Move

sealed class GameAdjudicationResult {
    object PlayOn : GameAdjudicationResult()

    class Win(
        val winner: Player,
        // TODO
        val reason: WinReason = WinReason.Checkmate
    ) : GameAdjudicationResult() {
        enum class WinReason {
            Checkmate
        }
    }

    class Draw(val drawReason: DrawReason) : GameAdjudicationResult() {
        enum class DrawReason {
            Stalemate,
            FiftyMovesRule
        }
    }

    class PlayMove(val move: Move) : GameAdjudicationResult()
}
