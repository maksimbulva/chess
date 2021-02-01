package ru.maksimbulva.chess.core.engine.move.generator

import ru.maksimbulva.chess.core.engine.board.Cell
import ru.maksimbulva.chess.core.engine.board.Vector2
import ru.maksimbulva.chess.core.engine.move.Move
import ru.maksimbulva.chess.core.engine.position.Position

internal abstract class BaseDeltasMoveGenerator : IMoveGenerator {

    protected fun generateMoves(
        fromCell: Cell,
        position: Position,
        deltas: Array<Vector2>,
        moves: MutableList<Move>
    ) {
        for (delta in deltas) {
            val toCell = fromCell + delta
            if (toCell != null
                && !position.board.isOccupiedByPlayer(toCell, position.playerToMove)
            ) {
                moves.add(Move(fromCell, toCell))
            }
        }
    }
}
