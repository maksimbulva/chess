package ru.maksimbulva.chess.core.engine.board

import ru.maksimbulva.chess.core.engine.Piece
import ru.maksimbulva.chess.core.engine.Player

class PieceOnBoard(
    val player: Player,
    val piece: Piece,
    val cell: Cell
)
