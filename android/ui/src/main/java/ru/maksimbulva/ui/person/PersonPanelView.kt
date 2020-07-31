package ru.maksimbulva.ui.person

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import kotlinx.android.synthetic.main.person_panel.view.*
import ru.maksimbulva.ui.R

class PersonPanelView(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int)
    : FrameLayout(context, attributeSet, defStyleAttr) {

    @StringRes private var nameResId: Int = NO_ID
    @DrawableRes private var portraitResId: Int = NO_ID

    constructor(context: Context) : this(context, attributeSet = null, defStyleAttr = 0)
    constructor(context: Context, attributeSet: AttributeSet)
        : this(context, attributeSet, defStyleAttr = 0)

    init {
        View.inflate(context, R.layout.person_panel, this)
    }

    fun setName(@StringRes nameResId: Int) {
        if (nameResId != this.nameResId) {
            person_name.text = context.getString(nameResId)
            this.nameResId = nameResId
        }
    }

    fun setPortrait(@DrawableRes portraitResId: Int) {
        if (portraitResId != this.portraitResId) {
            person_portrait.setImageResource(portraitResId)
            this.portraitResId = portraitResId
        }
    }

    fun setEvaluation(value: Double?) {
        evaluation.text = value?.toInt().toString()
    }
}
