package ru.maksimbulva.chess.core.engine.board

import ru.maksimbulva.chess.core.engine.Piece
import ru.maksimbulva.chess.core.engine.Player

private const val NODES_MEMORY_BUFFER_CAPACITY = 16

private val DUMMY_PIECE_VALUE = PieceOnBoard(Player.Black, Piece.King, Cell(0))

internal class PieceLinkedList(
    val player: Player,
    kingCell: Cell
) : Iterable<PieceOnBoard> {

    private var _head = Node(PieceOnBoard(player, Piece.King, kingCell))
    val head: Node
        get() = _head

    private val nodesMemoryBuffer = mutableListOf<Node>().apply {
        addAll((0 until NODES_MEMORY_BUFFER_CAPACITY).map { Node(DUMMY_PIECE_VALUE) })
    }

    val king: PieceOnBoard
        get() = _head.value

    override fun iterator(): Iterator<PieceOnBoard> {
        return PieceIterator(_head)
    }

    internal fun addPiece(pieceToAdd: PieceOnBoard): Node {
        val newNode = nodesMemoryBuffer.removeAt(nodesMemoryBuffer.lastIndex).apply {
            value = pieceToAdd
            prev = _head
            next = _head.next
        }
        _head.next?.let { it.prev = newNode }
        _head.next = newNode
        return newNode
    }

    fun remove(node: Node) {
        node.prev?.next = node.next
        node.next?.prev = node.prev
        nodesMemoryBuffer.add(node)
    }

    internal class Node(
        var value: PieceOnBoard
    ) {
        var prev: Node? = null
        var next: Node? = null
    }

    private class PieceIterator(head: Node) : Iterator<PieceOnBoard> {
        private var nextNode: Node? = head

        override fun hasNext() = nextNode != null

        override fun next(): PieceOnBoard {
            val result = nextNode!!
            nextNode = result.next
            return result.value
        }
    }
}
