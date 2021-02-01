package ru.maksimbulva.chess.core.engine.move.generator

import ru.maksimbulva.chess.core.PlayerMap
import ru.maksimbulva.chess.core.engine.Piece
import ru.maksimbulva.chess.core.engine.Player
import ru.maksimbulva.chess.core.engine.board.Vector2
import ru.maksimbulva.chess.core.engine.move.Move
import ru.maksimbulva.chess.core.engine.position.Position

object MoveGenerator {

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

    private val pawnMoveGenerators = PlayerMap(
        PawnMoveGenerator(Player.Black),
        PawnMoveGenerator(Player.White)
    )

    private val kingMoveGenerators = PlayerMap(
        KingMoveGenerator(Player.Black),
        KingMoveGenerator(Player.White)
    )

    private val bishopMoveGenerator = RayMoveGenerator(bishopRayMoves)
    private val rookMoveGenerator = RayMoveGenerator(rookRayMoves)
    private val queenMoveGenerator = RayMoveGenerator(queenRayMoves)

    fun generateMoves(position: Position): List<Move> {
        return generatePseudoLegalMoves(position)
            .filter { move -> isLegalMove(position, move) }
    }

    private fun generatePseudoLegalMoves(position: Position): List<Move> {
        val moves = mutableListOf<Move>()
        val board = position.board
        for (pieceOnBoard in board.pieces(position.playerToMove)) {
            pieceMoveGenerator(pieceOnBoard.piece, pieceOnBoard.player).generateMoves(
                pieceOnBoard.cell,
                position,
                moves
            )
        }
        return moves
    }

    private fun pieceMoveGenerator(piece: Piece, player: Player): IMoveGenerator {
        return when (piece) {
            Piece.Pawn -> pawnMoveGenerators.get(player)
            Piece.Knight -> KnightMoveGenerator
            Piece.Bishop -> bishopMoveGenerator
            Piece.Rook -> rookMoveGenerator
            Piece.Queen -> queenMoveGenerator
            Piece.King -> kingMoveGenerators.get(player)
        }
    }

    private fun isLegalMove(position: Position, move: Move): Boolean {
        val newPosition = position.playMove(move)
        return !isAttacksCell(
            position = newPosition,
            attacker = newPosition.playerToMove,
            targetCell = newPosition.board.kingCell(position.playerToMove)
        )
    }
}
