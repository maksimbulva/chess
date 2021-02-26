package ru.maksimbulva.chess.core.engine.board

import ru.maksimbulva.chess.core.PlayerMap
import ru.maksimbulva.chess.core.collections.BufferedLinkedList
import ru.maksimbulva.chess.core.engine.Piece
import ru.maksimbulva.chess.core.engine.Player

private typealias LinkedListNode = BufferedLinkedList.Node<PieceOnBoard>

internal class PieceTable(pieces: Iterable<PieceOnBoard>) {

    private val table = arrayOfNulls<LinkedListNode>(size = Board.BOARD_CELL_COUNT)

    private val playerPieces = PlayerMap(
        blackPlayerValue = createPlayerPiecesLinkedList(pieces, Player.Black),
        whitePlayerValue = createPlayerPiecesLinkedList(pieces, Player.White)
    )

    init {
        fillTableWithPieces(playerPieces.get(Player.Black))
        fillTableWithPieces(playerPieces.get(Player.White))
    }

    fun isEmpty(cell: Cell) = table[cell.index] == null

    fun isNotEmpty(cell: Cell) = table[cell.index] != null

    fun pieces(player: Player): Iterable<PieceOnBoard> = playerPieces.get(player)

    fun kingCell(player: Player) = playerPieces.get(player).head?.value?.cell

    fun pieceAt(cell: Cell): PieceOnBoard? = table[cell.index]?.value

    fun movePiece(fromCell: Cell, toCell: Cell) {
        val nodeToMove = pieceNodeAt(fromCell)
        val pieceToMove = nodeToMove.value
        nodeToMove.value = PieceOnBoard(pieceToMove.player, pieceToMove.piece, toCell)
        table[fromCell.index] = null
        table[toCell.index] = nodeToMove
    }

    fun removePieceAt(cell: Cell) {
        val nodeToRemove = pieceNodeAt(cell)
        playerPieces.get(nodeToRemove.value.player).remove(nodeToRemove)
        table[cell.index] = null
    }

    fun insertPiece(pieceOnBoard: PieceOnBoard) {
        val piecesLinkedList = playerPieces.get(pieceOnBoard.player)
        table[pieceOnBoard.cell.index] = if (pieceOnBoard.piece == Piece.King) {
            piecesLinkedList.insertFirst(pieceOnBoard)
        } else {
            piecesLinkedList.insertAfterHead(pieceOnBoard)
        }
    }

    fun updatePiece(cell: Cell, newPiece: Piece) {
        with (pieceNodeAt(cell)) {
            table[cell.index]!!.value = PieceOnBoard(value.player, newPiece, value.cell)
        }
    }

    private fun fillTableWithPieces(piecesLinkedList: BufferedLinkedList<PieceOnBoard>) {
        var currentNode = piecesLinkedList.head
        while (currentNode != null) {
            table[currentNode.value.cell.index] = currentNode
            currentNode = currentNode.next
        }
    }

    private fun pieceNodeAt(cell: Cell) = table[cell.index]!!
}

private fun createPlayerPiecesLinkedList(
    pieces: Iterable<PieceOnBoard>,
    player: Player
): BufferedLinkedList<PieceOnBoard> {
    val king = pieces.first { it.piece == Piece.King && it.player == player }
    return BufferedLinkedList<PieceOnBoard>().apply {
        insertFirst(king)
        pieces.asSequence()
            .filter { it.piece != Piece.King && it.player == player }
            .forEach(this::insertAfterHead)
    }
}
