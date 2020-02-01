package ru.maksimbulva.chess

import ru.maksimbulva.chess.core.engine.Engine
import ru.maksimbulva.chess.core.engine.position.Position

class ChessEngineService {

    private val engine = Engine()

    val currentPosition: Position
        get() = engine.currentPosition
}