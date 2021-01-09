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

    constructor(piecesOnBoard: Sequence<PieceOnBoard>) : this(
        Array<ColoredPiece?>(size = BOARD_CELL_COUNT) { null }.apply {
            piecesOnBoard.forEach {
                this[it.cell.index] = it.coloredPiece
            }
        }
    )

    init {
        require(cells.size == BOARD_CELL_COUNT)

        cells.forEachIndexed { index, coloredPiece ->
            if (coloredPiece != null) {
                if (coloredPiece.player == Player.Black) {
                    blackOccupiedCells = blackOccupiedCells.setBit(index)
                } else {
                    whiteOccupiedCells = whiteOccupiedCells.setBit(index)
                }
            }
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

    fun rowSequence(row: Int): Sequence<ColoredPiece?> {
        return (0..7).asSequence().map { column -> pieceAt(row, column) }
    }

    fun pieceAt(row: Int, column: Int): ColoredPiece? = cells[Cell.encode(row, column)]

    fun pieceAt(cell: Cell): ColoredPiece? = cells[cell.index]

    fun pieces(player: Player): Iterable<PieceOnBoard> {
        // TODO - optimize me
        return cells
            .mapIndexed { index, coloredPiece ->
                if (coloredPiece?.player == player) {
                    PieceOnBoard(coloredPiece, Cell(index))
                } else {
                    null
                }
            }
            .filterNotNull()
    }

    fun kingCell(player: Player): Cell {
        // TODO - optimize me
        return pieces(player)
            .first { it.coloredPiece.piece == Piece.King }
            .cell
    }

    fun playMove(move: Move, player: Player): Board {
        require(!isEmpty(move.fromCell))
        val newCells = cells.clone()
        val movingPiece = pieceAt(move.fromCell)
        newCells[move.fromCell.index] = null
        newCells[move.toCell.index] = if (move.promoteTo == null) {
            movingPiece
        } else {
            ColoredPiece(player, move.promoteTo)
        }

        if (move.isEnPassantCapture) {
            val capturedCell = Cell(move.fromCell.row, move.toCell.column)
            newCells[capturedCell.index] = null
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
        }

        return Board(newCells)
    }

    companion object {
        const val BOARD_CELL_COUNT = 64

        const val ROW_COUNT = 8
        const val COLUMN_COUNT = 8
    }
}