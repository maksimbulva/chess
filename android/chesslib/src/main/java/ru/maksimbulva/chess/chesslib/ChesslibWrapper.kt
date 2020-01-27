package ru.maksimbulva.chess.chesslib

import ru.maksimbulva.chess.core.engine.Variation
import ru.maksimbulva.chess.core.engine.move.Move
import ru.maksimbulva.chess.core.notation.CoordinateNotation

class ChesslibWrapper : AbsChesslibWrapper() {

    private val enginePointer = createEngineInstance()

    fun destroy() {
        releaseEngineInstance(enginePointer)
    }

    fun resetGame() {
        resetGame(enginePointer)
    }

    fun resetGame(fen: String) {
        resetGame(fen, enginePointer)
    }

    fun currentPositionFen(): String = getCurrentPositionFen(enginePointer)

    fun playMove(move: Move): Boolean {
        return playMove(CoordinateNotation.moveToString(move), enginePointer)
    }

    fun findBestVariation(): Variation {
        TODO()
    }
}
