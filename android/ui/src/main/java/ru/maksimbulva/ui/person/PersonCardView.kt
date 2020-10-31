package ru.maksimbulva.ui.person

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import ru.maksimbulva.ui.R

class PersonCardView(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int)
    : FrameLayout(context, attributeSet, defStyleAttr) {

    private val portraitImage: ImageView
    private val nameText: TextView

    constructor(context: Context) : this(context, attributeSet = null, defStyleAttr = 0)
    constructor(context: Context, attributeSet: AttributeSet)
            : this(context, attributeSet, defStyleAttr = 0)

    init {
        View.inflate(context, R.layout.person_card, this)

        portraitImage = findViewById(R.id.person_card_portrait)
        nameText = findViewById(R.id.person_card_name)
    }

    fun setItem(item: PersonCardItem) {
        portraitImage.setImageResource(item.portrait)
        nameText.text = item.personName
    }
}
