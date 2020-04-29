package ru.maksimbulva.ui.move_list.items

import android.content.Context
import android.view.ViewGroup
import ru.maksimbulva.ui.common.SimpleAdapter

internal class MoveListAdapter(private val context: Context)
    : SimpleAdapter<MoveListItemViewHolder, MoveListItem>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoveListItemViewHolder {
        return MoveListItemViewHolder(context, parent)
    }

    override fun onBindViewHolder(holder: MoveListItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
