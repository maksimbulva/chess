package tools.pgn

import chess.engine.Engine
import chess.engine.fen.FenEncoder

object PgnPositionsImporter {

    fun importPositions(filename: String, verbose: Boolean) {
        val engine = Engine()
        PgnGamesImporter.importFromFile(filename, verbose)
            .take(1)
            .forEach { game ->
                engine.resetToInitialPosition()
                game.moves.forEach { move ->
                    engine.playMove(move)
                    val encodedPosition = FenEncoder.encode(
                        engine.currentPosition,
                        FenEncoder.EncodingOptions.SetMovesCountToOne
                    )
                    println(encodedPosition)
                }
            }
    }
}
