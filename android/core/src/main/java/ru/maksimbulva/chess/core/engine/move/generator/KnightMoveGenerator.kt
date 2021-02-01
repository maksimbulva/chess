package ru.maksimbulva.chess.core.engine.move.generator

import ru.maksimbulva.chess.core.engine.board.Cell
import ru.maksimbulva.chess.core.engine.board.Vector2
import ru.maksimbulva.chess.core.engine.move.Move
import ru.maksimbulva.chess.core.engine.position.Position

internal object KnightMoveGenerator : BaseDeltasMoveGenerator() {

    private val KNIGHT_DELTAS = arrayOf(
        Vector2(-1, -2),
        Vector2(1, -2),
        Vector2(-2, -1),
        Vector2(2, -1),
        Vector2(-2, 1),
        Vector2(2, 1),
        Vector2(-1, 2),
        Vector2(1, 2)
    )

    override fun generateMoves(fromCell: Cell, position: Position, moves: MutableList<Move>) {
        return generateMoves(fromCell, position, KNIGHT_DELTAS, moves)
    }
}
