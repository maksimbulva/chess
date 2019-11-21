package database

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection

class DatabaseInteractor {

    private val db = Database.connect("jdbc:sqlite:chess.db", "org.sqlite.JDBC")

    init {
        TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
    }

    fun foo() {
        transaction {
            addLogger(StdOutSqlLogger)

            SchemaUtils.create(Positions)

            println("Positions: ${Positions.selectAll()}")
        }
    }

    fun countPositions(): Int {
        var result: Int = -1
        transaction {
            result = Positions.slice(Positions.position).selectAll().count()
        }
        require(result >= 0)
        return result
    }

    fun appendPositions(positions: List<String>): Int {
        val oldPositions = readPositions()
        val addedPositions = mutableSetOf<String>()
        var positionsAppendedCounter = 0

        positions.asSequence()
            .filter { it !in oldPositions && it !in addedPositions }
            .forEach { newPosition ->
                transaction {
                    Positions.insert {
                        it[position] = newPosition
                    }
                    commit()
                }
                positionsAppendedCounter += 1
                addedPositions.add(newPosition)
            }

        return positionsAppendedCounter
    }

    private fun readPositions(): Set<String> {
        var result: Set<String> = emptySet()
        transaction {
            result = Positions.slice(Positions.position).selectAll()
                .asSequence()
                .map { it[Positions.position] }
                .toSet()
        }
        return result
    }

    object Positions : IntIdTable() {
        val position = varchar("position", length = 80).uniqueIndex()
        val evaluation = float("evaluation").nullable()
    }
}
