package ru.maksimbulva.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import kotlin.math.max
import kotlin.math.min
import kotlin.math.round

class PersonPanel(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int)
    : FrameLayout(context, attributeSet, defStyleAttr) {

    private val view =
        LayoutInflater.from(context).inflate(R.layout.person_panel, this, true)

    private val portraitImage: ImageView = view.findViewById(R.id.person_portrait)
    private val evaluationText: TextView = view.findViewById(R.id.evaluation)

    constructor(context: Context) : this(context, attributeSet = null, defStyleAttr = 0)
    constructor(context: Context, attributeSet: AttributeSet)
        : this(context, attributeSet, defStyleAttr = 0)

    fun setPortrait(@DrawableRes portraitResId: Int) {
        portraitImage.setImageResource(portraitResId)
    }

    fun updateEvaluation(model: EvaluationModel) {
        evaluationText.text =
            context.getString(R.string.evaluation_format, model.evaluationAsPercents)
    }

    class EvaluationModel(
        val evaluation: Double
    ) {
        val evaluationAsPercents: Int
            get() {
                // TODO: for now, just keep the original value unchanged
                return evaluation.toInt()
            }
    }
}
