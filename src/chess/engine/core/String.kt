package chess.engine.core

private val _rowToString: Array<String> = (1..8).map { it.toString() }.toTypedArray()
private val _columnToString: Array<String> = (0..7).map { ('a' + it).toString() }.toTypedArray()

fun rowToString(row: Int) = _rowToString[row]
fun columnToString(column: Int) = _columnToString[column]