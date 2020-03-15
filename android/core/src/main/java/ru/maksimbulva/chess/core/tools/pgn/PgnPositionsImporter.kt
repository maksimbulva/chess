package ru.maksimbulva.chess.core.tools.pgn

import ru.maksimbulva.chess.core.engine.Engine
import ru.maksimbulva.chess.core.engine.fen.FenEncoder
import ru.maksimbulva.chess.core.engine.move.DetailedMovesFactory

object PgnPositionsImporter {

    fun importPositions(filename: String, verbose: Boolean): Sequence<String> {
        val engine = Engine()
        return PgnGamesImporter.importFromFile(filename, verbose)
            .asSequence()
            .flatMap { game ->

                val encodedPositions = mutableListOf<String>()
                engine.resetToInitialPosition()

                game.moves.forEach { detailedMove ->
                    engine.playMove(DetailedMovesFactory.convertToMove(detailedMove))
                    val encodedPosition = FenEncoder.encode(
                        engine.currentPosition,
                        FenEncoder.EncodingOptions.SetMovesCountToOne
                    )
                    encodedPositions.add(encodedPosition)
                }

                encodedPositions.asSequence()
            }
    }
}
