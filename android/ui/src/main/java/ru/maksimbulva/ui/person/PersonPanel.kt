package ru.maksimbulva.ui.person

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import ru.maksimbulva.ui.R

class PersonPanel(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int)
    : FrameLayout(context, attributeSet, defStyleAttr) {

    private val view =
        LayoutInflater.from(context).inflate(R.layout.person_panel, this, true)

    private val portraitImage: ImageView = view.findViewById(R.id.person_portrait)
    private val personNameText: TextView = view.findViewById(R.id.person_name)
    private val evaluationText: TextView = view.findViewById(R.id.evaluation)

    private var currentState: PersonPanelState? = null

    constructor(context: Context) : this(context, attributeSet = null, defStyleAttr = 0)
    constructor(context: Context, attributeSet: AttributeSet)
        : this(context, attributeSet, defStyleAttr = 0)

    fun setState(state: PersonPanelState) {
        if (state.portraitResId != currentState?.portraitResId) {
            portraitImage.setImageResource(state.portraitResId)
        }

        if (state.nameResId != currentState?.nameResId) {
            personNameText.text = context.getString(state.nameResId)
        }

        evaluationText.text = state.evaluation?.toInt().toString()

        currentState = state
    }
}
