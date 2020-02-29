package ru.maksimbulva.ui.chessboard

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.GridView
import ru.maksimbulva.chess.core.engine.Piece
import ru.maksimbulva.chess.core.engine.Player
import ru.maksimbulva.ui.R
import ru.maksimbulva.ui.chessboard.items.ChessboardAdapter
import ru.maksimbulva.ui.chessboard.items.ChessboardItem
import java.util.*
import kotlin.math.min

class ChessboardView(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
    : FrameLayout(context, attrs, defStyleAttr) {

    private val grid: GridView

    // TODO: Allow this to be set from outside
    private val theme = ChessboardTheme(
        squareDark = R.drawable.chessboard_square_fritz_dark,
        squareLight = R.drawable.chessboard_square_fritz_light,
        pieces = EnumMap<Player, EnumMap<Piece, Int>>(mapOf(
            Player.Black to EnumMap<Piece, Int>(mapOf<Piece, Int>(
                Piece.Pawn to R.drawable.piece_set_alpha_black_pawn,
                Piece.Knight to R.drawable.piece_set_alpha_black_knight,
                Piece.Bishop to R.drawable.piece_set_alpha_black_bishop,
                Piece.Rook to R.drawable.piece_set_alpha_black_rook,
                Piece.Queen to R.drawable.piece_set_alpha_black_queen,
                Piece.King to R.drawable.piece_set_alpha_black_king
            )),
            Player.White to EnumMap<Piece, Int>(mapOf<Piece, Int>(
                Piece.Pawn to R.drawable.piece_set_alpha_white_pawn,
                Piece.Knight to R.drawable.piece_set_alpha_white_knight,
                Piece.Bishop to R.drawable.piece_set_alpha_white_bishop,
                Piece.Rook to R.drawable.piece_set_alpha_white_rook,
                Piece.Queen to R.drawable.piece_set_alpha_white_queen,
                Piece.King to R.drawable.piece_set_alpha_white_king
            ))
        ))
    )

    private val adapter = ChessboardAdapter(context, theme)

    constructor(context: Context) : this(context, attrs = null, defStyleAttr = 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, defStyleAttr = 0)

    init {
        LayoutInflater.from(context).inflate(R.layout.chessboard, this, true)

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
