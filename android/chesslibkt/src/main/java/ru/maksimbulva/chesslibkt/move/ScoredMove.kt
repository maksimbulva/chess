package ru.maksimbulva.chesslibkt.move

import ru.maksimbulva.chesslibkt.Player
import ru.maksimbulva.chesslibkt.evaluate.Evaluator

class ScoredMove(
    val move: Move,
    val evaluator: Evaluator,
    val player: Player
)
