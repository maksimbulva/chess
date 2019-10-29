package pgn

import chess.engine.core.move.Move

class PgnGame(
    val tags: Map<String, String>,
    val moves: List<Move>
)
