package ru.maksimbulva.ui.person

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import ru.maksimbulva.ui.R

class PersonCardView(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int)
    : FrameLayout(context, attributeSet, defStyleAttr) {

    constructor(context: Context) : this(context, attributeSet = null, defStyleAttr = 0)
    constructor(context: Context, attributeSet: AttributeSet)
            : this(context, attributeSet, defStyleAttr = 0)

    init {
        View.inflate(context, R.layout.person_card, this)
    }
}
