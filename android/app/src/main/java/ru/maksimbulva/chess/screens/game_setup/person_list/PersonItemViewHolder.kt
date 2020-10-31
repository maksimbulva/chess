package ru.maksimbulva.chess.screens.game_setup.person_list

import android.view.View
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import ru.maksimbulva.chess.R
import ru.maksimbulva.ui.person.PersonCardItem
import ru.maksimbulva.ui.person.PersonCardView

class PersonItemViewHolder(
    itemView: View,
    private val onClick: (PersonCardItem) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    private val personCardView: PersonCardView =
        itemView.findViewById(R.id.person_list_item_person_card)

    private val checkButton: ImageButton = itemView.findViewById(R.id.person_list_item_check)

    fun bind(item: PersonCardItem) {
        personCardView.setItem(item)
        checkButton.setOnClickListener { onClick(item) }
    }
}
