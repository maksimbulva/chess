package ru.maksimbulva.chess.screens.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.maksimbulva.chess.core.engine.ColoredPiece
import ru.maksimbulva.chess.core.engine.board.Board
import ru.maksimbulva.chess.core.engine.board.Cell
import ru.maksimbulva.chess.core.engine.position.Position
import ru.maksimbulva.ui.chessboard.items.ChessboardItem

class GameScreenViewModel : ViewModel() {
    private val _chessboardItems: MutableLiveData<List<ChessboardItem>> = MutableLiveData()

    val chessboarItems: LiveData<List<ChessboardItem>>
        get() = _chessboardItems

    private var _position: Position? = null
    var position: Position?
        get() = _position
        set(value) {
            _position = value
            _position?.let {
                _chessboardItems.value = generateChessboardItems(it.board)
            }
        }

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
