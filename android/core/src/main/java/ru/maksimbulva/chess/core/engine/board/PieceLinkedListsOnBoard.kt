package ru.maksimbulva.chess.core.engine.board

import ru.maksimbulva.chess.core.engine.ColoredPiece
import ru.maksimbulva.chess.core.engine.Piece
import ru.maksimbulva.chess.core.engine.Player

internal class PieceLinkedListsOnBoard(pieces: Array<ColoredPiece?>) {
    private val nodesBoard = Array<PieceLinkedList.Node?>(Board.BOARD_CELL_COUNT) { null }

    private val blackPieceLinkedList = createPieceLinkedList(pieces, Player.Black)
    private val whitePieceLinkedList = createPieceLinkedList(pieces, Player.White)

    fun pieceLinkedList(player: Player): PieceLinkedList {
        return if (player == Player.Black) {
            blackPieceLinkedList
        } else {
            whitePieceLinkedList
        }
    }

    fun movePiece(from: Cell, dest: Cell) {
        removePieceAt(dest)
        val nodeToMove = nodesBoard[from.index]!!
        val pieceToMove = nodeToMove.value
        nodeToMove.value = PieceOnBoard(pieceToMove.player, pieceToMove.piece, dest)
        nodesBoard[from.index] = null
        nodesBoard[dest.index] = nodeToMove
    }

    fun updatePieceAt(cell: Cell, piece: Piece) {
        val nodeToUpdate = nodesBoard[cell.index]!!
        val oldPiece = nodeToUpdate.value
        nodeToUpdate.value = PieceOnBoard(oldPiece.player, piece, cell)
    }

    fun removePieceAt(cell: Cell) {
        val nodeToRemove = nodesBoard[cell.index] ?: return
        val pieceToRemove = nodeToRemove.value
        pieceLinkedList(pieceToRemove.player).remove(nodeToRemove)
        nodesBoard[pieceToRemove.cell.index] = null
    }

    private fun createPieceLinkedList(
        pieces: Array<ColoredPiece?>,
        player: Player
    ): PieceLinkedList {
        val kingCellIndex = pieces.indexOfFirst {
            it?.player == player && it.piece == Piece.King
        }
        require(kingCellIndex >= 0)

        return PieceLinkedList(player, Cell(kingCellIndex)).apply {
            nodesBoard[kingCellIndex] = head
            pieces.asSequence()
                .mapIndexedNotNull { index, coloredPiece ->
                    if (coloredPiece != null && coloredPiece.player == player &&
                            coloredPiece.piece != Piece.King) {
                        PieceOnBoard(player, coloredPiece.piece, Cell(index))
                    } else {
                        null
                    }
                }
                .forEach {
                    nodesBoard[it.cell.index] = addPiece(it)
                }
        }
    }
}
