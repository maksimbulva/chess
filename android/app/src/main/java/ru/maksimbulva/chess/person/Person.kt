package ru.maksimbulva.chess.person

import ru.maksimbulva.chess.core.engine.Piece

sealed class Person {
    class Human : Person()

    class Computer(
        val evaluationsLimit: Long = DEFAULT_EVALUATIONS_LIMIT,
        val degreeOfRandomness: Int = 0,
        val materialValue: Map<Piece, Int> = emptyMap()
    ) : Person()

    companion object {
        private const val DEFAULT_EVALUATIONS_LIMIT: Long = 1_000_000
    }
}
