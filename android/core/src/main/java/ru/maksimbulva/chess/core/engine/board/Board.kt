package ru.maksimbulva.chess.core.engine.board

import ru.maksimbulva.chess.core.engine.Bitmask64
import ru.maksimbulva.chess.core.engine.ColoredPiece
import ru.maksimbulva.chess.core.engine.Piece
import ru.maksimbulva.chess.core.engine.Player
import ru.maksimbulva.chess.core.engine.move.Move

class Board(private val cells: Array<ColoredPiece?>) {

    private var blackOccupiedCells = Bitmask64(0L)
    private var whiteOccupiedCells = Bitmask64(0L)
    private var occupiedCells = Bitmask64(0L)

    private val piecesLists = PieceLinkedListsOnBoard(cells)

    constructor(piecesOnBoard: Sequence<PieceOnBoard>) : this(
        Array<ColoredPiece?>(size = BOARD_CELL_COUNT) { null }.apply {
            piecesOnBoard.forEach {
                this[it.cell.index] = ColoredPiece(it.player, it.piece)
            }
        }
    )

    init {
        require(cells.size == BOARD_CELL_COUNT)

        for (piece in piecesLists.pieceLinkedList(Player.Black)) {
            blackOccupiedCells = blackOccupiedCells.setBit(piece.cell.index)
        }

        for (piece in piecesLists.pieceLinkedList(Player.White)) {
            whiteOccupiedCells = whiteOccupiedCells.setBit(piece.cell.index)
        }

        occupiedCells = Bitmask64(blackOccupiedCells.value or whiteOccupiedCells.value)
    }

    fun isEmpty(row: Int, column: Int): Boolean {
        return isEmpty(Cell(row, column))
    }

    fun isEmpty(cell: Cell): Boolean = !occupiedCells[cell.index]

    fun isOccupiedByPlayer(cell: Cell, player: Player): Boolean {
        return if (player == Player.Black) {
            blackOccupiedCells[cell.index]
        } else {
            whiteOccupiedCells[cell.index]
        }
    }

    fun pieceAt(row: Int, column: Int): PieceOnBoard? = pieceAt(Cell(row, column))

    fun pieceAt(cell: Cell): PieceOnBoard? = piecesLists.pieceAt(cell)

    fun pieces(player: Player): Iterable<PieceOnBoard> {
        return piecesLists.pieceLinkedList(player)
    }

    fun kingCell(player: Player): Cell {
        return piecesLists.pieceLinkedList(player).king.cell
    }

    fun playMove(move: Move, player: Player): Board {
        require(!isEmpty(move.fromCell))
        val newCells = cells.clone()
        val movingPiece = pieceAt(move.fromCell)!!
        newCells[move.fromCell.index] = null
//        piecesLists.movePiece(move.fromCell, move.toCell)
        newCells[move.toCell.index] = if (move.isPawnPromotion) {
            ColoredPiece(player, move.promoteTo!!)
        } else {
//            piecesLists.updatePieceAt(move.toCell, move.promoteTo)
            ColoredPiece(movingPiece.player, movingPiece.piece)
        }

        if (move.isEnPassantCapture) {
            val capturedCell = Cell(move.fromCell.row, move.toCell.column)
            newCells[capturedCell.index] = null
//            piecesLists.removePieceAt(capturedCell)
        }

        if (move.isCastle) {
            val rookColumn = if (move.fromCell.column < move.toCell.column) 7 else 0
            val rookFromCell = Cell(move.fromCell.row, rookColumn)
            require(newCells[rookFromCell.index] == ColoredPiece(player, Piece.Rook))

            val rookToCell = Cell(
                move.fromCell.row,
                (move.fromCell.column + move.toCell.column) / 2
            )

            val movingRook = newCells[rookFromCell.index]
            newCells[rookFromCell.index] = null
            newCells[rookToCell.index] = movingRook
//            piecesLists.movePiece(rookFromCell, rookToCell)
        }

        return Board(newCells)
    }

    companion object {
        const val BOARD_CELL_COUNT = 64

        const val ROW_COUNT = 8
        const val COLUMN_COUNT = 8
    }
}