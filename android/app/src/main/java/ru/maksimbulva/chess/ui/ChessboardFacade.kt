package ru.maksimbulva.chess.ui

import ru.maksimbulva.chess.core.engine.ColoredPiece
import ru.maksimbulva.chess.core.engine.Player
import ru.maksimbulva.chess.core.engine.board.Board
import ru.maksimbulva.chess.core.engine.board.Cell
import ru.maksimbulva.ui.chessboard.ChessboardView
import ru.maksimbulva.ui.chessboard.items.ChessboardItem

internal class ChessboardFacade(private val view: ChessboardView) {

    fun update(board: Board, playerOnTop: Player) {
        view.setItems(generateItemsForBoard(board, playerOnTop))
    }

    companion object {
        private fun generateItemsForBoard(board: Board, playerOnTop: Player): List<ChessboardItem> {
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

        private fun createChessboardItem(cell: Cell, coloredPiece: ColoredPiece?): ChessboardItem {
            return ChessboardItem(
                cell = cell,
                player = coloredPiece?.player,
                piece = coloredPiece?.piece
            )
        }
    }
}
