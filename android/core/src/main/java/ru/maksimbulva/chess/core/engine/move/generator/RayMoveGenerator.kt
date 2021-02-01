package ru.maksimbulva.chess.core.engine.move.generator

import ru.maksimbulva.chess.core.engine.board.Cell
import ru.maksimbulva.chess.core.engine.board.Vector2
import ru.maksimbulva.chess.core.engine.move.Move
import ru.maksimbulva.chess.core.engine.move.MoveBuilder
import ru.maksimbulva.chess.core.engine.otherPlayer
import ru.maksimbulva.chess.core.engine.position.Position

internal class RayMoveGenerator(
    private val rayDeltas: Array<Vector2>
) : IMoveGenerator {

    override fun generateMoves(fromCell: Cell, position: Position, moves: MutableList<Move>) {
        for (rayDelta in rayDeltas) {
            generateRayMoves(fromCell, position, rayDelta, moves)
        }
    }

    private fun generateRayMoves(
        fromCell: Cell,
        position: Position,
        rayDelta: Vector2,
        moves: MutableList<Move>
    ) {
        val otherPlayer = position.playerToMove.otherPlayer()
        val board = position.board
        val moveBuilder = MoveBuilder(fromCell)
        var curCell: Cell? = fromCell + rayDelta
        while (curCell != null) {
            if (board.isEmpty(curCell)) {
                moves.add(moveBuilder.setToCell(curCell).build())
            } else if (board.isOccupiedByPlayer(curCell, otherPlayer)) {
                moves.add(moveBuilder.setToCell(curCell).build())
                break
            } else {
                break
            }
            curCell += rayDelta
        }
    }
}