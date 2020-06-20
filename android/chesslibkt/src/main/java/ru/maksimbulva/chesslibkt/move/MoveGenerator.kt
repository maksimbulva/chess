package ru.maksimbulva.chesslibkt.move

import ru.maksimbulva.chesslibkt.Piece
import ru.maksimbulva.chesslibkt.Player
import ru.maksimbulva.chesslibkt.board.Board
import ru.maksimbulva.chesslibkt.board.Square
import ru.maksimbulva.chesslibkt.getOtherPlayer
import ru.maksimbulva.chesslibkt.position.Position

object MoveGenerator {

    private val promotionPieces = arrayOf(Piece.Knight, Piece.Bishop, Piece.Rook, Piece.Queen)

    private const val BlackInitialPawnRow = 6
    private const val WhiteInitialPawnRow = 1

    private val pawnCaptureDeltaColumns = arrayOf(-1, 1)

    fun fillWithPseudoLegalMoves(
        moves: MutableList<Move>,
        movesFilter: MoveGenerationFilter,
        position: Position) {

        check(moves.isEmpty())

        val playerToMove = position.playerToMove
        position.board.getPlayerPieces(playerToMove).forEach { boardSquare ->
            when (boardSquare.piece) {
                Piece.Pawn -> {
                    fillWithPawnMoves(
                        moves, movesFilter, position, boardSquare.square
                    )
                }
                Piece.Knight -> Unit
                Piece.Bishop -> Unit
                Piece.Rook -> Unit
                Piece.Queen -> Unit
                Piece.King -> Unit
            }
        }
    }

    private fun fillWithPawnMoves(
        moves: MutableList<Move>,
        movesFilter: MoveGenerationFilter,
        position: Position,
        pawnSquare: Square) {

        val playerToMove = position.playerToMove
        val moveBuilder = MoveBuilder(Piece.Pawn, pawnSquare)

        val board = position.board

        val forward = when (playerToMove) {
            Player.Black -> Direction.Down
            Player.White -> Direction.Up
        }

        val prePromotionRow = when (playerToMove) {
            Player.Black -> 1
            Player.White -> Board.ROW_MAX - 1
        }

        val singleMoveForwardSquare = pawnSquare + forward
        if (movesFilter == MoveGenerationFilter.AllMoves) {
            if (board.isEmpty(singleMoveForwardSquare)) {
                if (pawnSquare.row == prePromotionRow) {
                    generatePromotions(moveBuilder.setDestSquare(singleMoveForwardSquare), moves)
                }
                else {
                    moves.add(moveBuilder.setDestSquare(singleMoveForwardSquare).build())
                    val initialRow = when (playerToMove) {
                        Player.Black -> BlackInitialPawnRow
                        Player.White -> WhiteInitialPawnRow
                    }
                    if (pawnSquare.row == initialRow) {
                        val doubleMoveForwardSquare = pawnSquare + forward + forward
                        if (board.isEmpty(doubleMoveForwardSquare)) {
                            moves.add(
                                moveBuilder.setDestSquare(doubleMoveForwardSquare)
                                    .setPawnDoubleMove()
                                    .build())
                        }
                    }
                }
            }
        }

        // Capture
        pawnCaptureDeltaColumns.forEach { deltaColumn ->
            val destColumn = pawnSquare.column + deltaColumn
            if (destColumn in Board.ColumnsRange) {
                val captureSquare = pawnSquare + forward + Direction(0, deltaColumn)
                if (board.isNotEmptyAndOtherPlayer(captureSquare, playerToMove)) {
                    val mb = moveBuilder
                        .setDestSquare(captureSquare)
                        .setCapture(board)
                    if (pawnSquare.row == prePromotionRow) {
                        generatePromotions(mb, moves)
                    }
                    else {
                        moves.add(mb.build())
                    }
                }
            }
        }

        // En passant capture
        val enPassantColumn = position.enPassantColumn
        if (enPassantColumn != null) {
            val deltaColumn = pawnSquare.column - enPassantColumn
            if (deltaColumn == -1 || deltaColumn == 1) {
                val enPassantCaptureRow = when (playerToMove) {
                    Player.Black -> 3
                    Player.White -> 4
                }
                if (pawnSquare.row == enPassantCaptureRow) {
                    val enemyPawnSquare = Square(pawnSquare.row, enPassantColumn)
                    val isEnemyPawnOnAttackedSquare = board.isPlayerPiece(
                        enemyPawnSquare,
                        getOtherPlayer(playerToMove),
                        Piece.Pawn
                    )
                    if (isEnemyPawnOnAttackedSquare) {
                        val captureSquare = Square(
                            singleMoveForwardSquare.row,
                            enPassantColumn
                        )
                        moves.add(
                            moveBuilder
                                .setDestSquare(captureSquare)
                                .setEnPassantCapture()
                                .build()
                        )
                    }
                }
            }
        }
    }

    private fun generatePromotions(moveBuilder: MoveBuilder, moves: MutableList<Move>) {
        promotionPieces.forEach { piece ->
            moves.add(moveBuilder.setPromoteToPieceType(piece).build())
        }
    }
}
