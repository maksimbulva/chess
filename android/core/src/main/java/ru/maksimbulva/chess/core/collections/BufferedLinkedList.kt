package ru.maksimbulva.chess.core.collections

import java.util.*

internal class BufferedLinkedList<E> : Iterable<E> {

    private val nodeBuffer = Stack<Node<E>>()

    var head: Node<E>? = null
        private set

    fun insertFirst(value: E): Node<E> {
        val currentHead = head
        val newHead = makeNewNode(value, prev = null, next = currentHead)
        currentHead?.prev = newHead
        head = newHead
        return newHead
    }

    fun insertAfterHead(value: E): Node<E> {
        val currentHead = head
        return if (currentHead == null) {
            insertFirst(value)
        } else {
            val newNode = makeNewNode(value, prev = currentHead, next = currentHead.next)
            currentHead.next = newNode
            newNode.next?.prev = newNode
            newNode
        }
    }

    fun remove(nodeToRemove: Node<E>) {
        if (nodeToRemove === head) {
            head = null
        } else {
            nodeToRemove.prev?.next = nodeToRemove.next
            nodeToRemove.next?.prev = nodeToRemove.prev
        }
        nodeBuffer.push(nodeToRemove)
    }

    private fun makeNewNode(value: E, prev: Node<E>?, next: Node<E>?): Node<E> {
        return if (nodeBuffer.empty()) {
            Node(value, prev, next)
        } else {
            nodeBuffer.pop().apply {
                this.value = value
                this.prev = prev
                this.next = next
            }
        }
    }

    override fun iterator(): Iterator<E> {
        return object : Iterator<E> {
            private var currentElement = head

            override fun hasNext() = currentElement != null

            override fun next(): E {
                val result = currentElement!!.value
                currentElement = currentElement!!.next
                return result!!
            }
        }
    }

    fun valuesIterator(): Iterator<E> {
        return object : Iterator<E> {
            private var currentElement = head

            override fun hasNext() = currentElement != null

            override fun next(): E {
                val result = currentElement!!.value
                currentElement = currentElement!!.next
                return result
            }
        }
    }

    class Node<E>(
        var value: E,
        var prev: Node<E>?,
        var next: Node<E>?
    )
}
