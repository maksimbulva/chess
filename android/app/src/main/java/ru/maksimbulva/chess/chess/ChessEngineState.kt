package ru.maksimbulva.chess.chess

import ru.maksimbulva.chess.core.PlayerMap
import ru.maksimbulva.chess.core.engine.Variation
import ru.maksimbulva.chess.core.engine.move.DetailedMove
import ru.maksimbulva.chess.core.engine.move.Move
import ru.maksimbulva.chess.core.engine.position.Position
import ru.maksimbulva.chess.person.Person

class ChessEngineState(
    val players: PlayerMap<Person>,
    val position: Position,
    val moveHistory: List<DetailedMove>,
    val bestVariation: Variation? = null
) {
    val bestMove: Move?
        get() = bestVariation?.moves?.firstOrNull()
}
