package ru.maksimbulva.chess.core.engine.move.generator

import ru.maksimbulva.chess.core.engine.board.Cell
import ru.maksimbulva.chess.core.engine.board.Vector2
import ru.maksimbulva.chess.core.engine.move.Move
import ru.maksimbulva.chess.core.engine.move.MoveBuilder
import ru.maksimbulva.chess.core.engine.otherPlayer
import ru.maksimbulva.chess.core.engine.position.Position

internal abstract class BaseDeltasMoveGenerator : IMoveGenerator {

    protected fun generateMoves(
        fromCell: Cell,
        position: Position,
        deltas: Array<Vector2>,
        moves: MutableList<Move>
    ) {
        val board = position.board
        val otherPlayer = position.playerToMove.otherPlayer()
        val moveBuilder = MoveBuilder(fromCell)
        for (delta in deltas) {
            val toCell = fromCell + delta
            if (toCell != null) {
                val moveBuilderForCurrentDelta = moveBuilder.setToCell(toCell)
                if (board.isEmpty(toCell)) {
                    moves.add(moveBuilderForCurrentDelta.build())
                } else {
                    val pieceAtToCell = board.pieceAt(toCell)
                    if (pieceAtToCell?.player == otherPlayer) {
                        moves.add(
                            moveBuilderForCurrentDelta.setAsCapture(pieceAtToCell.piece).build()
                        )
                    }
                }
            }
        }
    }
}
