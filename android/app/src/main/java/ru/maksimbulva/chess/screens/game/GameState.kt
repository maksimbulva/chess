package ru.maksimbulva.chess.screens.game

import ru.maksimbulva.chess.chess.GameAdjudicationResult
import ru.maksimbulva.chess.core.PlayerMap
import ru.maksimbulva.chess.core.engine.move.DetailedMove
import ru.maksimbulva.chess.person.Person

data class GameState(
    val players: PlayerMap<Person>,
    val moveHistory: List<DetailedMove>,
    val adjudicationResult: GameAdjudicationResult
)
