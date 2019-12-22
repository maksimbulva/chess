package ru.maksimbulva.chess.core.engine.board

import ru.maksimbulva.chess.core.engine.columnToString
import ru.maksimbulva.chess.core.engine.rowToString

data class Cell(val row: Int, val column: Int) {
    val index: Int = row * 8 + column

    constructor(index: Int) : this(index shr 3, index and 7)

    operator fun plus(vec: Vector2): Cell? {
        val newRow = row + vec.deltaRow
        val newColumn = column + vec.deltaColumn
        return if (newRow in 0..7 && newColumn in 0..7) {
            Cell(newRow, newColumn)
        } else {
            null
        }
    }

    operator fun minus(vec: Vector2): Cell? {
        val newRow = row - vec.deltaRow
        val newColumn = column - vec.deltaColumn
        return if (newRow in 0..7 && newColumn in 0..7) {
            Cell(newRow, newColumn)
        } else {
            null
        }
    }

    operator fun minus(other: Cell) = Vector2(row - other.row, column - other.column)

    override fun toString(): String {
        return columnToString(column) + rowToString(row)
    }

    companion object {
        fun encode(row: Int, column: Int) = (row shl 3) + column

        fun of(cellStr: String): Cell {
            require(cellStr.length == 2) { "$cellStr cannot be parsed as Cell" }
            return Cell(row = rowOf(cellStr[1]), column = columnOf(cellStr[0]))
        }
    }
}

fun rowOf(rowChar: Char): Int {
    require(rowChar in '1'..'8')
    return rowChar - '1'
}

fun columnOf(columnChar: Char): Int {
    require(columnChar in 'a'..'h')
    return columnChar - 'a'
}
