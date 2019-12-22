package ru.maksimbulva.chess.core.tools.pgn

import ru.maksimbulva.chess.core.pgn.PgnGame
import ru.maksimbulva.chess.core.pgn.PgnParser
import java.io.File

object PgnGamesImporter {

    fun importFromFile(filename: String, verbose: Boolean): List<PgnGame> {
        if (verbose) {
            println("Importing PGN games from $filename")
        }
        try {
            File(filename).useLines { linesSequence ->
                val result = PgnParser.parse(linesSequence.iterator())
                println("Imported ${result.size} games")
                return result
            }
        }
        catch (e: Exception) {
            if (verbose) {
                println(e)
            }
            throw e
        }
    }
}
