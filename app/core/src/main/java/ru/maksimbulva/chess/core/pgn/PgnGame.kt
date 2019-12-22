package ru.maksimbulva.chess.core.pgn

import ru.maksimbulva.chess.core.engine.move.Move

class PgnGame(
    val tags: Map<String, String>,
    val moves: List<Move>
)
