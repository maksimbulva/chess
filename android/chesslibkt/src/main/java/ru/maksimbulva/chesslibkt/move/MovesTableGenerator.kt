package ru.maksimbulva.chesslibkt.move

import ru.maksimbulva.chesslibkt.board.Board
import ru.maksimbulva.chesslibkt.board.Square

internal fun generateMovesTable(directions: Array<Vector2>): Array<IntArray> {
    val table = Board.allSquares
        .map { filterByDestSquareIsValid(directions, it) }
        .map { it.map { vector2 -> Direction(vector2).encoded }.toIntArray() }
        .toList()
    val uniqueTableElements = table.toSet()
    return table
        .map { tableElement ->
            uniqueTableElements.first { it.contentEquals(tableElement) }
        }
        .toTypedArray()
}

private fun filterByDestSquareIsValid(directions: Array<Vector2>, square: Square): Array<Vector2> {
    return directions.filter {
        val row = it.deltaRow + square.row
        val column = it.deltaColumn + square.column
        row in Board.RowsRange && column in Board.ColumnsRange
    }
        .toTypedArray()
}
