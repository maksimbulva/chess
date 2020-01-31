package ru.maksimbulva.ui.chessboard

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.GridView
import ru.maksimbulva.ui.R
import ru.maksimbulva.ui.chessboard.items.ChessboardAdapter
import ru.maksimbulva.ui.chessboard.items.ChessboardItem
import kotlin.math.min

class ChessboardView(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
    : FrameLayout(context, attrs, defStyleAttr) {

    private val grid: GridView

    // TODO: Allow this to be set from outside
    private val theme = ChessboardTheme(
        squareDark = R.drawable.chessboard_square_fritz_dark,
        squareLight = R.drawable.chessboard_square_fritz_light
    )

    private val adapter = ChessboardAdapter(context, theme)

    constructor(context: Context) : this(context, attrs = null, defStyleAttr = 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, defStyleAttr = 0)

    init {
        LayoutInflater.from(context).inflate(R.layout.chessboard, this, true)
        setBackgroundColor(resources.getColor(R.color.green))

        grid = findViewById(R.id.chessboard_grid)
        grid.adapter = adapter
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val minSize = min(measuredWidth, measuredHeight)
        setMeasuredDimension(minSize, minSize)
    }

    fun setItems(items: List<ChessboardItem>) {
        adapter.setItems(items)
    }
}
