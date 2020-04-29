package ru.maksimbulva.ui.replay.items

import android.content.Context
import android.view.ViewGroup
import ru.maksimbulva.ui.common.SimpleAdapter

internal class ReplayGameControlsAdapter(
    private val context: Context,
    private val onClickListener: (ReplayGameControlItem) -> Unit
) : SimpleAdapter<ReplayGameControlViewHolder, ReplayGameControlItem>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReplayGameControlViewHolder {
        return ReplayGameControlViewHolder(context, parent, onClickListener)
    }

    override fun onBindViewHolder(holder: ReplayGameControlViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
