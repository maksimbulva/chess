package ru.maksimbulva.ui.person

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.person_panel.view.*
import ru.maksimbulva.ui.R

class PersonPanelView(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int)
    : FrameLayout(context, attributeSet, defStyleAttr) {

    private var currentState: PersonPanelState? = null

    constructor(context: Context) : this(context, attributeSet = null, defStyleAttr = 0)
    constructor(context: Context, attributeSet: AttributeSet)
        : this(context, attributeSet, defStyleAttr = 0)

    init {
        View.inflate(context, R.layout.person_panel, this)
    }

    fun setState(state: PersonPanelState) {
        if (state.portraitResId != currentState?.portraitResId) {
            person_portrait.setImageResource(state.portraitResId)
        }

        if (state.nameResId != currentState?.nameResId) {
            person_name.text = context.getString(state.nameResId)
        }

        evaluation.text = state.evaluation?.toInt().toString()

        currentState = state
    }
}
