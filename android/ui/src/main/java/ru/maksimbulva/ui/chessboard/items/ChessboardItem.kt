package ru.maksimbulva.ui.chessboard.items

import ru.maksimbulva.chess.core.engine.Piece
import ru.maksimbulva.chess.core.engine.Player
import ru.maksimbulva.chess.core.engine.board.Cell

class ChessboardItem(
    val cell: Cell,
    val player: Player?,
    val piece: Piece?
)
