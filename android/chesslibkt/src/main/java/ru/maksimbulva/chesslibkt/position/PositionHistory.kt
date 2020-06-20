package ru.maksimbulva.chesslibkt.position

import ru.maksimbulva.chesslibkt.move.Move

class PositionHistory(
    val move: Move,
    val enPassantColumn: Int?,
    val positionFlags: PositionFlags,
    val halfmoveClock: Int,
    val fullmoveNumber: Int
)
