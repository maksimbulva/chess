package ru.maksimbulva.chess.core.engine.move

import ru.maksimbulva.chess.core.engine.ColoredPiece
import ru.maksimbulva.chess.core.engine.Piece
import ru.maksimbulva.chess.core.engine.Player
import ru.maksimbulva.chess.core.engine.board.Board
import ru.maksimbulva.chess.core.engine.board.Cell
import ru.maksimbulva.chess.core.engine.board.Vector2
import ru.maksimbulva.chess.core.engine.otherPlayer
import ru.maksimbulva.chess.core.engine.position.Position
import kotlin.math.abs

object MoveGenerator {

    private val pawnCaptureColumnDeltas = arrayOf(-1, 1)

    private val knightDeltas = arrayOf(
        Vector2(-1, -2),
        Vector2(1, -2),
        Vector2(-2, -1),
        Vector2(2, -1),
        Vector2(-2, 1),
        Vector2(2, 1),
        Vector2(-1, 2),
        Vector2(1, 2)
    )

    private val bishopRayMoves = arrayOf(
        Vector2(-1, -1),
        Vector2(-1, 1),
        Vector2(1, -1),
        Vector2(1, 1)
    )

    private val rookRayMoves = arrayOf(
        Vector2(-1, 0),
        Vector2(0, -1),
        Vector2(0, 1),
        Vector2(1, 0)
    )

    private val queenRayMoves = bishopRayMoves + rookRayMoves

    private val kingDeltas = arrayOf(
        Vector2(-1, -1),
        Vector2(0, -1),
        Vector2(1, -1),
        Vector2(-1, 0),
        Vector2(1, 0),
        Vector2(-1, 1),
        Vector2(0, 1),
        Vector2(1, 1)
    )

    private val pawnPromotions = arrayOf(
        Piece.Knight,
        Piece.Bishop,
        Piece.Rook,
        Piece.Queen
    )

    private const val kingColumn = 4

    fun generateMoves(position: Position): List<Move> {
        return generatePseudoLegalMoves(position)
            .filter { move ->
                val newPosition = position.playMove(move)
                !isAttacksCell(
                    position = newPosition,
                    attacker = newPosition.playerToMove,
                    targetCell = newPosition.board.kingCell(position.playerToMove)
                )
            }
    }

    private fun isAttacksCell(position: Position, attacker: Player, targetCell: Cell): Boolean {
        val board = position.board
        return board.pieces(attacker).any { pieceOnBoard ->
            val fromCell = pieceOnBoard.cell
            val delta = targetCell - fromCell
            when (pieceOnBoard.coloredPiece.piece) {
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

    private fun generatePseudoLegalMoves(position: Position): List<Move> {
        val moves = mutableListOf<Move>()
        val board = position.board
        board.pieces(position.playerToMove).forEach { pieceOnBoard ->
            when (pieceOnBoard.coloredPiece.piece) {
                Piece.Pawn -> {
                    generatePawnMoves(pieceOnBoard.cell, position, moves)
                }
                Piece.Knight -> {
                    generateMoves(
                        pieceOnBoard.cell,
                        position,
                        knightDeltas,
                        moves
                    )
                }
                Piece.Bishop -> {
                    bishopRayMoves.forEach { rayDelta ->
                        generateRayMoves(
                            pieceOnBoard.cell,
                            position,
                            rayDelta,
                            moves
                        )
                    }
                }
                Piece.Rook -> {
                    rookRayMoves.forEach { rayDelta ->
                        generateRayMoves(
                            pieceOnBoard.cell,
                            position,
                            rayDelta,
                            moves
                        )
                    }
                }
                Piece.Queen -> {
                    queenRayMoves.forEach { rayDelta ->
                        generateRayMoves(
                            pieceOnBoard.cell,
                            position,
                            rayDelta,
                            moves
                        )
                    }
                }
                Piece.King -> {
                    generateMoves(
                        pieceOnBoard.cell,
                        position,
                        kingDeltas,
                        moves
                    )
                    val castlings = position.castlingAvailability(position.playerToMove)
                    if (castlings.canCastleShort || castlings.canCastleLong) {
                        val kingCell = if (position.playerToMove == Player.Black) {
                            Cell.of("e8")
                        } else {
                            Cell.of("e1")
                        }
                        if (board.pieceAt(kingCell) == ColoredPiece(position.playerToMove, Piece.King)) {
                            if (castlings.canCastleShort) {
                                generateCastleMove(position, 7, moves)
                            }
                            if (castlings.canCastleLong) {
                                generateCastleMove(position, 0, moves)
                            }
                        }
                    }
                }
            }
        }
        return moves
    }

    private fun generatePawnMoves(
        fromCell: Cell,
        position: Position,
        moves: MutableList<Move>
    ) {
        val board = position.board
        val deltaRow = if (position.playerToMove == Player.Black) -1 else 1
        val moveForwardCell = fromCell + Vector2(deltaRow, 0)
        if (moveForwardCell != null && board.isEmpty(moveForwardCell)) {
            generatePawnMoveOrPromotions(fromCell, moveForwardCell, moves)

            if ((position.playerToMove == Player.Black && fromCell.row == 6)
                || (position.playerToMove == Player.White && fromCell.row == 1)
            ) {
                val moveForwardTwiceCell = moveForwardCell + Vector2(deltaRow, 0)
                if (moveForwardTwiceCell != null && board.isEmpty(moveForwardTwiceCell)) {
                    moves.add(Move(fromCell, moveForwardTwiceCell))
                }
            }
        }

        val otherPlayer = position.playerToMove.otherPlayer()
        pawnCaptureColumnDeltas.forEach { deltaColumn ->
            val captureCell = fromCell + Vector2(deltaRow, deltaColumn)
            if (captureCell != null && board.isOccupiedByPlayer(captureCell, otherPlayer)) {
                generatePawnMoveOrPromotions(fromCell, captureCell, moves)
            }
        }

        if (position.enPassantCaptureColumn != null
            && abs(fromCell.column - position.enPassantCaptureColumn) == 1
            && ((position.playerToMove == Player.Black && fromCell.row == 3)
                    || (position.playerToMove == Player.White && fromCell.row == 4))
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
        if (toCell.row == 0 || toCell.row == 7) {
            pawnPromotions.forEach {
                moves.add(Move(fromCell, toCell, promoteTo = it))
            }
        } else {
            moves.add(Move(fromCell, toCell))
        }
    }

    private fun generateMoves(
        fromCell: Cell,
        position: Position,
        deltas: Array<Vector2>,
        moves: MutableList<Move>
    ) {
        deltas.forEach { delta ->
            val toCell = fromCell + delta
            if (toCell != null
                && !position.board.isOccupiedByPlayer(toCell, position.playerToMove)
            ) {
                moves.add(Move(fromCell, toCell))
            }
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
        var curCell: Cell? = fromCell + rayDelta
        while (curCell != null) {
            if (board.isEmpty(curCell)) {
                moves.add(Move(fromCell, curCell))
            } else if (board.isOccupiedByPlayer(curCell, otherPlayer)) {
                moves.add(Move(fromCell, curCell))
                break
            } else {
                break
            }
            curCell += rayDelta
        }
    }

    private fun generateCastleMove(
        position: Position,
        rookColumn: Int,
        moves: MutableList<Move>
    ) {
        val board = position.board
        val otherPlayer = position.playerToMove.otherPlayer()
        val baseRow = if (position.playerToMove == Player.Black) 7 else 0
        if (board.pieceAt(baseRow, rookColumn) != ColoredPiece(position.playerToMove, Piece.Rook)) {
            return
        }
        if (isAttacksCell(position, otherPlayer, Cell(baseRow, kingColumn))) {
            return
        }

        if (rookColumn == 0) {
            if ((1 until kingColumn).any { !board.isEmpty(baseRow, it) }) {
                return
            }
            if (isAttacksCell(position, otherPlayer, Cell(baseRow, kingColumn - 1))) {
                return
            }
            moves.add(Move.createLongCastle(position.playerToMove))

        } else {
            if (((kingColumn + 1) until 7).any { !board.isEmpty(baseRow, it) }) {
                return
            }
            if (isAttacksCell(position, otherPlayer, Cell(baseRow, kingColumn + 1))) {
                return
            }
            moves.add(Move.createShortCastle(position.playerToMove))
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
}
