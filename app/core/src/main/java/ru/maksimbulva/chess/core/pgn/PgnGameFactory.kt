package ru.maksimbulva.chess.core.pgn

import ru.maksimbulva.chess.core.engine.Engine

internal object PgnGameFactory {

    fun createGame(tags: Map<String, String>, unparsedMoves: List<String>): PgnGame {
        val engine = Engine()

        unparsedMoves.forEach { unparsedMove ->
            val moveNumberDotIndex = unparsedMove.indexOf('.')

            val moveString = if (moveNumberDotIndex >= 0) {
                unparsedMove.substring(moveNumberDotIndex + 1)
            } else {
                unparsedMove
            }

            val parsedMove = PgnMoveParser.parseMove(moveString)

            val currentPosition = engine.currentPosition

            val matchedLegalMoves = engine.legalMoves.filter {
                move -> parsedMove.isMatch(move, currentPosition)
            }
            if (matchedLegalMoves.size == 1) {
                engine.playMove(matchedLegalMoves.first())
            } else {
                throw PgnParseException("Cannot find legal move which matches pgn move $moveString")
            }
        }

        return PgnGame(tags.toMap(), engine.moveHistory)
    }

}