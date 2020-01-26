package ru.maksimbulva.chess.chesslib

import ru.maksimbulva.chess.core.engine.Variation

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

    fun playMove(move: String): Boolean {
        return playMove(move, enginePointer)
    }

    fun findBestVariation(): Variation {
        TODO()
    }
}
