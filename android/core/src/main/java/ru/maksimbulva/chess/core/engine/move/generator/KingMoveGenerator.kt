package ru.maksimbulva.chess.core.engine.move.generator

import ru.maksimbulva.chess.core.engine.Piece
import ru.maksimbulva.chess.core.engine.Player
import ru.maksimbulva.chess.core.engine.board.Cell
import ru.maksimbulva.chess.core.engine.board.Vector2
import ru.maksimbulva.chess.core.engine.move.Move
import ru.maksimbulva.chess.core.engine.otherPlayer
import ru.maksimbulva.chess.core.engine.position.Position

internal class KingMoveGenerator(private val player: Player) : BaseDeltasMoveGenerator() {
    private val otherPlayer = player.otherPlayer()

    private val baseRow = if (player == Player.Black) 7 else 0
    private val initialKingCell= Cell(baseRow, INITIAL_KING_COLUMN)

    private val shortCastleMove = Move(
        fromCell = initialKingCell,
        toCell = Cell(baseRow, 6),
        promoteTo = null,
        isEnPassantCapture = false,
        capturedPiece = null,
        isCastle = true
    )

    private val longCastleMove = Move(
        fromCell = initialKingCell,
        toCell = Cell(baseRow, 2),
        promoteTo = null,
        isEnPassantCapture = false,
        capturedPiece = null,
        isCastle = true
    )

    override fun generateMoves(fromCell: Cell, position: Position, moves: MutableList<Move>) {
        generateMoves(fromCell, position, KING_DELTAS, moves)
        generateCastleMoves(fromCell, position, moves)
    }

    private fun generateCastleMoves(fromCell: Cell, position: Position, moves: MutableList<Move>) {
        if (fromCell != initialKingCell) {
            return
        }

        val board = position.board
        val castlings = position.castlingAvailability(player)
        if (castlings.canCastleShort || castlings.canCastleLong) {
            val pieceOnKingCell = board.pieceAt(initialKingCell)
            if (pieceOnKingCell?.piece == Piece.King && pieceOnKingCell.player == player) {
                if (isAttacksCell(position, otherPlayer, initialKingCell)) {
                    return
                }

                if (castlings.canCastleShort) {
                    generateShortCastleMove(position, moves)
                }
                if (castlings.canCastleLong) {
                    generateLongCastleMove(position, moves)
                }
            }
        }
    }

    private fun generateShortCastleMove(position: Position, moves: MutableList<Move>) {
        val board = position.board
        val pieceAtRookCell = board.pieceAt(baseRow, 7) ?: return
        if (pieceAtRookCell.piece != Piece.Rook || pieceAtRookCell.player != player) {
            return
        }

        if (((INITIAL_KING_COLUMN + 1) until 7).any { !board.isEmpty(baseRow, it) }) {
            return
        }
        if (isAttacksCell(position, otherPlayer, Cell(baseRow, INITIAL_KING_COLUMN + 1))) {
            return
        }
        moves.add(shortCastleMove)
    }

    private fun generateLongCastleMove(position: Position, moves: MutableList<Move>) {
        val board = position.board
        val pieceAtRookCell = board.pieceAt(baseRow, 0) ?: return
        if (pieceAtRookCell.piece != Piece.Rook || pieceAtRookCell.player != player) {
            return
        }

        if ((1 until INITIAL_KING_COLUMN).any { !board.isEmpty(baseRow, it) }) {
            return
        }
        if (isAttacksCell(position, otherPlayer, Cell(baseRow, INITIAL_KING_COLUMN - 1))) {
            return
        }
        moves.add(longCastleMove)
    }
}

private const val INITIAL_KING_COLUMN = 4

private val KING_DELTAS = arrayOf(
    Vector2(-1, -1),
    Vector2(0, -1),
    Vector2(1, -1),
    Vector2(-1, 0),
    Vector2(1, 0),
    Vector2(-1, 1),
    Vector2(0, 1),
    Vector2(1, 1)
)
