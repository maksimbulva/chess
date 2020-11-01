package ru.maksimbulva.chess.person

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import ru.maksimbulva.chess.core.engine.Piece

sealed class Person(
    val id: Int,
    @DrawableRes val portrait: Int,
    @StringRes val nameResId: Int
) {

    class Human(
        @DrawableRes portrait: Int,
        @StringRes nameResId: Int
    ) : Person(PERSON_HUMAN_ID, portrait, nameResId)

    class Computer(
        id: Int,
        @DrawableRes portrait: Int,
        @StringRes nameResId: Int,
        val evaluationsLimit: Long = DEFAULT_EVALUATIONS_LIMIT,
        val degreeOfRandomness: Int = 0,
        val materialValue: Map<Piece, Int> = emptyMap()
    ) : Person(id, portrait, nameResId)

    companion object {
        private const val DEFAULT_EVALUATIONS_LIMIT: Long = 1_000_000

        const val PERSON_HUMAN_ID = 0
    }
}
