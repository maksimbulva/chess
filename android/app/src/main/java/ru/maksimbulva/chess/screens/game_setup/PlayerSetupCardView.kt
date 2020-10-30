package ru.maksimbulva.chess.screens.game_setup

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import ru.maksimbulva.chess.R

class PlayerSetupCardView(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int)
    : FrameLayout(context, attributeSet, defStyleAttr) {

    constructor(context: Context) : this(context, attributeSet = null, defStyleAttr = 0)
    constructor(context: Context, attributeSet: AttributeSet)
            : this(context, attributeSet, defStyleAttr = 0)

    init {
        View.inflate(context, R.layout.player_setup_card_view, this)

        if (attributeSet != null) {
            val styledAttrs = context.obtainStyledAttributes(
                attributeSet,
                R.styleable.PlayerSetupCardView,
                defStyleAttr,
                0
            )
            findViewById<TextView>(R.id.player_setup_title).text = styledAttrs.getString(
                R.styleable.PlayerSetupCardView_title
            )
            styledAttrs.recycle()
        }
    }
}
