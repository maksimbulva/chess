package ru.maksimbulva.chesslibkt.board

import ru.maksimbulva.chesslibkt.Piece
import ru.maksimbulva.chesslibkt.Player

class BoardSquare(
    var square: Square,
    var player: Player?,
    var piece: Piece?,
    var prev: BoardSquare?,
    var next: BoardSquare?
) {

    val isEmpty: Boolean
        get() = player == null

    constructor(square: Square, player: Player, piece: Piece)
        : this(square, player, piece, null, null)

    fun setPiece(player: Player, piece: Piece) {
        this.player = player
        this.piece = piece
    }
}
