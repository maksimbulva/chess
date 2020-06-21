package ru.maksimbulva.chesslibkt.board

import ru.maksimbulva.chesslibkt.move.Direction

inline class Square(val encoded: Int) {

    val row: Int
        get() = encoded shr 3

    val column: Int
        get() = encoded and 7

    constructor(row: Int, column: Int) : this((row shl 3) + column)

    operator fun plus(direction: Direction): Square {
        return Square(encoded + direction.encoded)
    }

    operator fun plus(encodedValue: Int) = Square(encoded + encodedValue)

    companion object {

        fun of(str: String): Square {
            require(str.length == 2)
            return Square(row = Board.rowOf(str.last()), column = Board.columnOf(str.first()))
        }
    }
}
