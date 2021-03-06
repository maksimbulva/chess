package ru.maksimbulva.chesslibkt.move

inline class Direction(val encoded: Int) {

    constructor(deltaRow: Int, deltaColumn: Int)
        : this(deltaRow * 8 + deltaColumn)

    internal constructor(vector2: Vector2)
        : this(vector2.deltaRow, vector2.deltaColumn)

    companion object {
        val Left = Direction(0, -1)
        val Right = Direction(0, 1)
        val Up = Direction(1, 0)
        val Down = Direction(-1, 0)
    }
}
