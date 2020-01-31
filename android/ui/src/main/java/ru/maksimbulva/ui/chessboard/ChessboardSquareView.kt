package ru.maksimbulva.ui.chessboard

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import ru.maksimbulva.ui.R
import ru.maksimbulva.ui.chessboard.items.ChessboardItem
import ru.maksimbulva.ui.core.IBindable

internal class ChessboardSquareView(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
    : FrameLayout(context, attrs, defStyleAttr), IBindable<ChessboardItem> {

    private val background: ImageView
    private val pieceImage: ImageView

    private lateinit var theme: ChessboardTheme

    constructor(context: Context) : this(context, attrs = null, defStyleAttr = 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, defStyleAttr = 0)

    init {
        LayoutInflater.from(context).inflate(R.layout.chessboard_cell, this, true)
        background = findViewById(R.id.chessboard_square_background)
        pieceImage = findViewById(R.id.chessboard_square_piece_image)
    }

    override fun bind(item: ChessboardItem) {
        background.setImageResource(when (item.cellColor) {
            ChessboardItem.CellColor.Black -> theme.squareDark
            ChessboardItem.CellColor.White -> theme.squareLight
        })
        if (item.player != null && item.piece != null) {
            val drawableId = theme.pieceImage(item.player, item.piece)
            pieceImage.setImageDrawable(ContextCompat.getDrawable(context, drawableId))
            pieceImage.isVisible = true
        } else {
            pieceImage.isVisible = false
        }
    }

    fun setTheme(theme: ChessboardTheme) {
        this.theme = theme
    }
}
