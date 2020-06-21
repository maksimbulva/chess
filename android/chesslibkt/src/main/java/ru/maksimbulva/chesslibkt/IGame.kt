package ru.maksimbulva.chesslibkt

import ru.maksimbulva.chesslibkt.move.Move
import ru.maksimbulva.chesslibkt.position.Position

interface IGame {
    val currentPosition: Position

    fun getLegalMoves(): List<Move>

    fun playMove(move: Move)

    fun undoMove()
}
