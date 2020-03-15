package ru.maksimbulva.chess.core.engine.move

import ru.maksimbulva.chess.core.engine.otherPlayer
import ru.maksimbulva.chess.core.engine.position.Position

object DetailedMovesFactory {

    fun create(move: Move, position: Position, nextPosition: Position): DetailedMove {
        val board = position.board
        val hasMovesInNextPosition = MoveGenerator.generateMoves(nextPosition).isNotEmpty()
        return DetailedMove(
            playerToMove = position.playerToMove,
            pieceToMove = board.pieceAt(move.fromCell)!!.piece,
            fromCell = move.fromCell,
            toCell = move.toCell,
            promoteTo = move.promoteTo,
            isCapture = move.isEnPassantCapture ||
                    board.pieceAt(move.toCell)?.player == position.playerToMove.otherPlayer(),
            isCheck = nextPosition.isInCheck,
            isCheckmate = nextPosition.isInCheck && !hasMovesInNextPosition,
            isEnPassantCapture = move.isEnPassantCapture,
            isShortCastle = move.isCastle && move.toCell.column == 2,
            isLongCastle = move.isCastle && move.toCell.column == 6,
            fullmoveNumber = position.fullMoveNumber
        )
    }

    fun convertToMove(detailedMove: DetailedMove) = Move(
        fromCell = detailedMove.fromCell,
        toCell = detailedMove.toCell,
        promoteTo = detailedMove.promoteTo,
        isEnPassantCapture = detailedMove.isEnPassantCapture,
        isCastle = detailedMove.isShortCastle || detailedMove.isLongCastle
    )
}