package ru.maksimbulva.chess.screens.game

import ru.maksimbulva.chess.core.engine.ColoredPiece
import ru.maksimbulva.chess.core.engine.board.Board
import ru.maksimbulva.chess.core.engine.board.Cell
import ru.maksimbulva.chess.core.engine.position.Position
import ru.maksimbulva.ui.chessboard.items.ChessboardItem

class GameScreenBoardState(val position: Position) {

    val chessboardItems = generateChessboardItems(position.board)

    companion object {
        private fun generateChessboardItems(board: Board): List<ChessboardItem> {
            return (7 downTo 0)
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
}
