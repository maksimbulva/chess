package ru.maksimbulva.chesslibkt.position

import ru.maksimbulva.chesslibkt.Piece
import ru.maksimbulva.chesslibkt.Player
import ru.maksimbulva.chesslibkt.board.PieceOnBoard

object PositionFactory {

    fun createPosition(
        pieces: Collection<PieceOnBoard>,
        playerToMove: Player,
        enPassantColumn: Int?,
        castleOptions: Map<Player, CastleOptions>,
        halfmoveClock: Int,
        fullmoveNumber: Int
    ): Position {
        val blackKing = pieces.find { it.player == Player.Black && it.pieceType == Piece.King }!!
        val whiteKing = pieces.find { it.player == Player.White && it.pieceType == Piece.King }!!

        return Position(
            blackKingSquare = blackKing.square,
            whiteKingSquare = whiteKing.square,
            playerToMove = playerToMove,
            enPassantColumn = enPassantColumn,
            castleOptions = castleOptions,
            halfmoveClock = halfmoveClock,
            fullmoveNumber = fullmoveNumber
        ).apply {
            pieces.asSequence().filter { it.pieceType != Piece.King } .forEach { addPiece(it) }
            optimizeCastleOptions()
            require(isValid())
        }
    }
}
