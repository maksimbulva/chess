package ru.maksimbulva.chesslibkt.board

import ru.maksimbulva.chesslibkt.Piece
import ru.maksimbulva.chesslibkt.Player

class BoardSquare(
    var player: Player?,
    var piece: Piece?,
    var prev: BoardSquare?,
    var next: BoardSquare?
) {

    val isEmpty: Boolean
        get() = player == null

    constructor(player: Player, piece: Piece)  : this(player, piece, null, null)

    fun setPiece(player: Player, piece: Piece) {
        this.player = player
        this.piece = piece
    }

    companion object {
        val EMPTY_INSTANCE = BoardSquare(
            player = null,
            piece = null,
            prev = null,
            next = null
        )
    }
}
