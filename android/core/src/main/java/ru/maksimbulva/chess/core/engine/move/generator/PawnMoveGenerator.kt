package ru.maksimbulva.chess.core.engine.move.generator

import ru.maksimbulva.chess.core.engine.Piece
import ru.maksimbulva.chess.core.engine.Player
import ru.maksimbulva.chess.core.engine.board.Board
import ru.maksimbulva.chess.core.engine.board.Cell
import ru.maksimbulva.chess.core.engine.board.Vector2
import ru.maksimbulva.chess.core.engine.move.Move
import ru.maksimbulva.chess.core.engine.move.MoveBuilder
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
        val moveBuilder = MoveBuilder(fromCell)

        val moveForwardCell = fromCell + moveForwardDelta
        if (moveForwardCell != null && board.isEmpty(moveForwardCell)) {
            if (moveForwardCell.row == promotionRow) {
                generatePromotions(moveBuilder.setToCell(moveForwardCell), moves)
            } else {
                moves.add(moveBuilder.setToCell(moveForwardCell).build())
            }

            if (fromCell.row == initialRow) {
                val moveForwardTwiceCell = moveForwardCell + moveForwardDelta
                if (moveForwardTwiceCell != null && board.isEmpty(moveForwardTwiceCell)) {
                    moves.add(moveBuilder.setToCell(moveForwardTwiceCell).build())
                }
            }
        }

        generateCaptures(fromCell, board, moves)

        if (position.enPassantCaptureColumn != null
            && (fromCell.row == enPassantCaptureRow)
            && abs(fromCell.column - position.enPassantCaptureColumn) == 1
        ) {
            val toCell = Cell(fromCell.row + deltaRow, position.enPassantCaptureColumn)
            moves.add(moveBuilder.setToCell(toCell).setAsEnPassantCapture().build())
        }
    }

    private fun generateCaptures(
        fromCell: Cell,
        board: Board,
        moves: MutableList<Move>
    ) {
        for (captureDelta in captureDeltas) {
            val captureCell = fromCell + captureDelta
            if (captureCell != null && !board.isEmpty(captureCell)) {
                val pieceAtCaptureCell = board.pieceAt(captureCell)
                if (pieceAtCaptureCell?.player == otherPlayer) {
                    val moveBuilder = MoveBuilder(fromCell)
                        .setToCell(captureCell)
                        .setAsCapture(pieceAtCaptureCell.piece)
                    if (captureCell.row == promotionRow) {
                        generatePromotions(moveBuilder, moves)
                    } else {
                        moves.add(moveBuilder.build())
                    }
                }
            }
        }
    }

    private fun generatePromotions(
        moveBuilder: MoveBuilder,
        moves: MutableList<Move>
    ) {
        for (promoteTo in PAWN_PROMOTIONS) {
            moves.add(moveBuilder.setPromoteTo(promoteTo).build())
        }
    }
}

private val PAWN_PROMOTIONS = arrayOf(
    Piece.Knight,
    Piece.Bishop,
    Piece.Rook,
    Piece.Queen
)
