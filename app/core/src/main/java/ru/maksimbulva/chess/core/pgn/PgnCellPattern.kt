package ru.maksimbulva.chess.core.pgn

import chess.engine.core.board.Cell
import chess.engine.core.board.columnOf
import chess.engine.core.board.rowOf

class PgnCellPattern(val row: Int? = null, val column: Int? = null) {

    fun isMatch(cell: Cell): Boolean {
        return (row == null || row == cell.row) &&
                (column == null || column == cell.column)
    }

    companion object {
        val EMPTY = PgnCellPattern(null, null)

        fun of(str: String): PgnCellPattern {
            return when (str.length) {
                0 -> EMPTY
                1 -> {
                    if (str[0].isDigit()) {
                        PgnCellPattern(row = rowOf(str[0]))
                    } else {
                        PgnCellPattern(column = columnOf(str[0]))
                    }
                }
                2 -> PgnCellPattern(row = rowOf(str[1]), column = columnOf(str[0]))
                else -> throw PgnParseException("`$str` is a badly encoded PGN cell")
            }
        }
    }
}
