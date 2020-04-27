package ru.maksimbulva.ui.move_list.items

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class MoveListAdapter(private val context: Context)
    : RecyclerView.Adapter<MoveListItemViewHolder>() {

    private var items = emptyList<MoveListItem>()

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoveListItemViewHolder {
        return MoveListItemViewHolder(context, parent)
    }

    override fun onBindViewHolder(holder: MoveListItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun setItems(items: List<MoveListItem>) {
        if (items != this.items) {
            this.items = items
            notifyDataSetChanged()
        }
    }
}
