package ru.maksimbulva.chess.person

sealed class Person {
    class Human : Person()

    class Computer(
        val evaluationsLimit: Long
    ) : Person()
}
