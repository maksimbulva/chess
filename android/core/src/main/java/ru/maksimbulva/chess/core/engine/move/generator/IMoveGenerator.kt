package ru.maksimbulva.chess.core.engine.move.generator

import ru.maksimbulva.chess.core.engine.board.Cell
import ru.maksimbulva.chess.core.engine.move.Move
import ru.maksimbulva.chess.core.engine.position.Position

internal interface IMoveGenerator {
    fun generateMoves(fromCell: Cell, position: Position, moves: MutableList<Move>)
}
