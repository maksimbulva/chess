package tools.pgn

import pgn.PgnGame
import pgn.PgnParser
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
