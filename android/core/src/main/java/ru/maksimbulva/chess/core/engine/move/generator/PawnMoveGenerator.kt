package ru.maksimbulva.chess.core.engine.move.generator

import ru.maksimbulva.chess.core.engine.Piece
import ru.maksimbulva.chess.core.engine.Player
import ru.maksimbulva.chess.core.engine.board.Cell
import ru.maksimbulva.chess.core.engine.board.Vector2
import ru.maksimbulva.chess.core.engine.move.Move
import ru.maksimbulva.chess.core.engine.otherPlayer
import ru.maksimbulva.chess.core.engine.position.Position
import kotlin.math.abs

internal class PawnMoveGenerator(
    private val player: Player
) : IMoveGenerator {

    private val initialRow = if (player == Player.Black) 6 else 1
    private val promotionRow = if (player == Player.Black) 0 else 7
    private val enPassantCaptureRow = if (player == Player.Black) 3 else 4

    private val deltaRow = if (player == Player.Black) -1 else 1
    private val moveForwardDelta = Vector2(deltaRow, 0)

    private val captureDeltas = arrayOf(
        Vector2(deltaRow, -1),
        Vector2(deltaRow, 1)
    )

    private val otherPlayer = player.otherPlayer()

    override fun generateMoves(fromCell: Cell, position: Position, moves: MutableList<Move>) {
        val board = position.board
        val moveForwardCell = fromCell + moveForwardDelta
        if (moveForwardCell != null && board.isEmpty(moveForwardCell)) {
            generatePawnMoveOrPromotions(fromCell, moveForwardCell, moves)

            if (fromCell.row == initialRow) {
                val moveForwardTwiceCell = moveForwardCell + moveForwardDelta
                if (moveForwardTwiceCell != null && board.isEmpty(moveForwardTwiceCell)) {
                    moves.add(Move(fromCell, moveForwardTwiceCell))
                }
            }
        }

        for (captureDelta in captureDeltas) {
            val captureCell = fromCell + captureDelta
            if (captureCell != null && board.isOccupiedByPlayer(captureCell, otherPlayer)) {
                generatePawnMoveOrPromotions(fromCell, captureCell, moves)
            }
        }

        if (position.enPassantCaptureColumn != null
            && (fromCell.row == enPassantCaptureRow)
            && abs(fromCell.column - position.enPassantCaptureColumn) == 1
        ) {

            val toCell = Cell(fromCell.row + deltaRow, position.enPassantCaptureColumn)
            moves.add(Move(fromCell, toCell, isEnPassantCapture = true))
        }
    }

    private fun generatePawnMoveOrPromotions(
        fromCell: Cell,
        toCell: Cell,
        moves: MutableList<Move>
    ) {
        if (toCell.row == promotionRow) {
            for (promoteTo in PAWN_PROMOTIONS) {
                moves.add(Move(fromCell, toCell, promoteTo))
            }
        } else {
            moves.add(Move(fromCell, toCell))
        }
    }
}

private val PAWN_PROMOTIONS = arrayOf(
    Piece.Knight,
    Piece.Bishop,
    Piece.Rook,
    Piece.Queen
)
