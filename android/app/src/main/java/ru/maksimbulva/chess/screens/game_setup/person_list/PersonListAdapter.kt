package ru.maksimbulva.chess.screens.game_setup.person_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.maksimbulva.chess.R
import ru.maksimbulva.ui.person.PersonCardItem

class PersonListAdapter(
    private val onClick: (PersonCardItem) -> Unit
) : RecyclerView.Adapter<PersonItemViewHolder>() {

    private var items = listOf<PersonCardItem>()

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.person_list_item_view,
            parent,
            false
        )
        return PersonItemViewHolder(itemView, onClick)
    }

    override fun onBindViewHolder(holder: PersonItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun setItems(items: List<PersonCardItem>) {
        this.items = items
        notifyDataSetChanged()
    }
}
