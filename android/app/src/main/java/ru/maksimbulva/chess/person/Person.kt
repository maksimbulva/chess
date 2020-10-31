package ru.maksimbulva.chess.person

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import ru.maksimbulva.chess.core.engine.Piece

sealed class Person(
    @DrawableRes val portrait: Int,
    @StringRes val nameResId: Int
) {

    class Human(
        @DrawableRes portrait: Int,
        @StringRes nameResId: Int
    ) : Person(portrait, nameResId)

    class Computer(
        val id: Int,
        @DrawableRes portrait: Int,
        @StringRes nameResId: Int,
        val evaluationsLimit: Long = DEFAULT_EVALUATIONS_LIMIT,
        val degreeOfRandomness: Int = 0,
        val materialValue: Map<Piece, Int> = emptyMap()
    ) : Person(portrait, nameResId)

    companion object {
        private const val DEFAULT_EVALUATIONS_LIMIT: Long = 1_000_000
    }
}
