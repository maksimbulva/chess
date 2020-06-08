package ru.maksimbulva.chesslibkt.board

inline class Square(val encoded: Int) {

    constructor(row: Int, column: Int) : this((row shl 3) + column)

    companion object {

        fun of(str: String): Square {
            require(str.length == 2)
            return Square(row = Board.rowOf(str.last()), column = Board.columnOf(str.first()))
        }
    }
}
