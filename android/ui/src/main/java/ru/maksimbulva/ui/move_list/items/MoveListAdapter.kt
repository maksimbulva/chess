package ru.maksimbulva.ui.move_list.items

import android.content.Context
import android.view.ViewGroup
import ru.maksimbulva.chess.core.engine.Player
import ru.maksimbulva.ui.common.SimpleAdapter

internal class MoveListAdapter(
    private val context: Context,
    private val onClickListener: (MoveListItem, Player) -> Unit
) : SimpleAdapter<MoveListItemViewHolder, MoveListItem>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoveListItemViewHolder {
        return MoveListItemViewHolder(context, parent, onClickListener)
    }

    override fun onBindViewHolder(holder: MoveListItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
