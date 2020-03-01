package ru.maksimbulva.chess.screens.game

import ru.maksimbulva.chess.core.engine.ColoredPiece
import ru.maksimbulva.chess.core.engine.Player
import ru.maksimbulva.chess.core.engine.board.Board
import ru.maksimbulva.chess.core.engine.board.Cell
import ru.maksimbulva.ui.chessboard.items.ChessboardItem

object ChessboardItemsGenerator {

    fun generateForBoard(board: Board, playerOnTop: Player): List<ChessboardItem> {
        return (if (playerOnTop == Player.Black) (7 downTo 0) else 0..7)
            .flatMap { row ->
                (0..7).map { column ->
                    val pieceAtCell: ColoredPiece? = board.pieceAt(row, column)
                    ChessboardItem(
                        cell = Cell(row, column),
                        player = pieceAtCell?.player,
                        piece = pieceAtCell?.piece,
                        cellColor = if ((row + column) % 2 == 0) {
                            ChessboardItem.CellColor.Black
                        } else {
                            ChessboardItem.CellColor.White
                        }
                    )
                }
            }
    }
}
