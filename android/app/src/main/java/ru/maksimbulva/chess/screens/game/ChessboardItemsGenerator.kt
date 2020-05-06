package ru.maksimbulva.chess.screens.game

import ru.maksimbulva.chess.core.engine.ColoredPiece
import ru.maksimbulva.chess.core.engine.Player
import ru.maksimbulva.chess.core.engine.board.Board
import ru.maksimbulva.chess.core.engine.board.Cell
import ru.maksimbulva.ui.chessboard.items.ChessboardItem

object ChessboardItemsGenerator {

    fun generateForBoard(board: Board, playerOnTop: Player): List<ChessboardItem> {
        return createCellsSequence(playerOnTop)
            .map { cell ->
                createChessboardItem(cell, board.pieceAt(cell))
            }
            .toList()
    }

    private fun createCellsSequence(playerOnTop: Player): Sequence<Cell> {
        return when (playerOnTop) {
            Player.Black -> 7 downTo 0
            Player.White -> 0..7
        }
            .flatMap { row -> (0..7).map { column -> Cell(row, column) } }
            .asSequence()
    }

    private fun getCellColor(cell: Cell): ChessboardItem.CellColor {
        return if ((cell.row + cell.column) % 2 == 0) {
            ChessboardItem.CellColor.Black
        } else {
            ChessboardItem.CellColor.White
        }
    }

    private fun createChessboardItem(cell: Cell, coloredPiece: ColoredPiece?): ChessboardItem {
        return ChessboardItem(
            cell = cell,
            player = coloredPiece?.player,
            piece = coloredPiece?.piece,
            cellColor = getCellColor(cell)
        )
    }
}
