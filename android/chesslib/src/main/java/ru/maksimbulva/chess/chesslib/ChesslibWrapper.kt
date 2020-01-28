package ru.maksimbulva.chess.chesslib

import ru.maksimbulva.chess.core.engine.Variation
import ru.maksimbulva.chess.core.engine.fen.FenDecoder
import ru.maksimbulva.chess.core.engine.move.Move
import ru.maksimbulva.chess.core.engine.move.MoveGenerator
import ru.maksimbulva.chess.core.engine.position.Position
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

    fun findBestVariation(onlyFirstMove: Boolean): Variation {
        val variationStringTokens = findBestVariation(enginePointer).split(' ')
        val evaluation = variationStringTokens.first().toInt()
        val moves = mutableListOf<Move>()
        var position = FenDecoder.decode(currentPositionFen())
        for (tokenIndex in 1 until variationStringTokens.size) {
            val variationMoveString = variationStringTokens[tokenIndex]
            val move = MoveGenerator.generateMoves(position)
                .first { move ->  CoordinateNotation.moveToString(move) == variationMoveString }
            moves.add(move)
            if (onlyFirstMove) {
                break
            }
            position.playMove(move)
        }
        return Variation(evaluation, moves)
    }
}
