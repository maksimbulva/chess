import database.DatabaseInteractor
import tools.pgn.PgnPositionsImporter

fun main(args: Array<String>) {
    val dbInteractor = DatabaseInteractor()
    dbInteractor.foo()

    println("There are ${dbInteractor.countPositions()} positions in the database")
/*
    val filename = "D:\\Chess\\pgn\\Karpov.pgn"
    val positions = PgnPositionsImporter.importPositions(filename, verbose = true).toList()
    println("Imported ${positions.size} positions from a file")

    val appendedPositionsCounter = dbInteractor.appendPositions(positions)

    println("Appended $appendedPositionsCounter positions")
 */
}
