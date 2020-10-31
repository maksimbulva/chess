package ru.maksimbulva.chess.screens.game_setup

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import ru.maksimbulva.chess.R
import ru.maksimbulva.chess.person.Person
import ru.maksimbulva.chess.person.convertToPersonCardItem
import ru.maksimbulva.ui.person.PersonCardItem
import ru.maksimbulva.ui.person.PersonCardView

class PlayerSetupCardView(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int)
    : FrameLayout(context, attributeSet, defStyleAttr) {

    private val personCardView: PersonCardView

    constructor(context: Context) : this(context, attributeSet = null, defStyleAttr = 0)
    constructor(context: Context, attributeSet: AttributeSet)
            : this(context, attributeSet, defStyleAttr = 0)

    init {
        View.inflate(context, R.layout.player_setup_card_view, this)

        personCardView = findViewById(R.id.player_setup_person_card)

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

    fun setPerson(person: Person?) {
        val item = if (person != null) {
            convertToPersonCardItem(context.resources, person)
        } else {
            PersonCardItem(
                personId = null,
                portrait = 0,
                personName = ""
            )
        }
        personCardView.setItem(item)
    }

    fun setOnReplacePlayerClickListener(listener: () -> Unit) {
        val button: Button = findViewById(R.id.player_setup_replace_player_button)
        button.setOnClickListener { listener() }
    }
}
