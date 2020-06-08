package ru.maksimbulva.chesslibkt.board

inline class Square(val encoded: Int) {

    constructor(row: Int, column: Int) : this((row shl 3) + column)
}
