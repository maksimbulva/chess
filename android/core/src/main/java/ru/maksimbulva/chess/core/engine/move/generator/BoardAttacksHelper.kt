package ru.maksimbulva.chess.core.engine.move.generator

import ru.maksimbulva.chess.core.engine.Piece
import ru.maksimbulva.chess.core.engine.Player
import ru.maksimbulva.chess.core.engine.board.Board
import ru.maksimbulva.chess.core.engine.board.Cell
import ru.maksimbulva.chess.core.engine.board.Vector2
import ru.maksimbulva.chess.core.engine.position.Position

internal fun isAttacksCell(position: Position, attacker: Player, targetCell: Cell): Boolean {
    val board = position.board
    return board.pieces(attacker).any { pieceOnBoard ->
        val fromCell = pieceOnBoard.cell
        val delta = targetCell - fromCell
        when (pieceOnBoard.piece) {
            Piece.Pawn -> delta.isPawnAttack(attacker)
            Piece.Knight -> delta.isKnightAttack()
            Piece.Bishop -> {
                delta.isBishopAttack() &&
                        isRayAttack(fromCell, targetCell, board, delta.toUnitLength())
            }
            Piece.Rook -> {
                delta.isRookAttack() &&
                        isRayAttack(fromCell, targetCell, board, delta.toUnitLength())
            }
            Piece.Queen -> {
                delta.isQueenAttack() &&
                        isRayAttack(fromCell, targetCell, board, delta.toUnitLength())
            }
            Piece.King -> delta.isKingAttack()
        }
    }
}

private fun isRayAttack(
    fromCell: Cell,
    targetCell: Cell,
    board: Board,
    rayDelta: Vector2
): Boolean {
    var curCell: Cell? = fromCell + rayDelta
    while (curCell != null && curCell.index != targetCell.index) {
        if (!board.isEmpty(curCell)) {
            return false
        }
        curCell += rayDelta
    }
    return true
}
