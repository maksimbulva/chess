package ru.maksimbulva.chess

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.maksimbulva.chess.core.engine.board.Cell
import ru.maksimbulva.ui.chessboard.ChessboardView
import ru.maksimbulva.ui.chessboard.items.ChessboardItem

class MainActivity : AppCompatActivity() {

    private lateinit var chessboard: ChessboardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        chessboard = findViewById(R.id.chessboard)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        val items = (7 downTo 0)
            .flatMap { row ->
                (0..7).map { column ->
                    ChessboardItem(
                        cell = Cell(row, column),
                        player = null,
                        piece = null,
                        cellColor = if ((row + column) % 2 == 0) {
                            ChessboardItem.CellColor.Black
                        } else {
                            ChessboardItem.CellColor.White
                        }
                    )
                }
            }

        chessboard.setItems(items)
    }
}
