package ru.maksimbulva.chess.core.engine.position.history

import ru.maksimbulva.chess.core.engine.move.Move
import ru.maksimbulva.chess.core.engine.position.CastlingAvailability

class PositionHistoryItem(
    val movePlayed: Move,
    val whiteCastlingAvailability: CastlingAvailability,
    val blackCastlingAvailability: CastlingAvailability,
    val enPassantCaptureColumn: Int?,
    val halfMoveClock: Int,
    val fullMoveNumber: Int
)
