package ru.maksimbulva.chess.screens.game

import ru.maksimbulva.chess.chess.GameAdjudicationResult
import ru.maksimbulva.chess.core.engine.move.DetailedMove

data class GameState(
    val moveHistory: List<DetailedMove>,
    val adjudicationResult: GameAdjudicationResult
)
