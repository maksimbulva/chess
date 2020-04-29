package ru.maksimbulva.ui.replay.items

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ru.maksimbulva.ui.R

internal class ReplayGameControlViewHolder(
    context: Context,
    parent: ViewGroup,
    private val onClickListener: (ReplayGameControlItem) -> Unit
) : RecyclerView.ViewHolder(
    LayoutInflater.from(context).inflate(
        R.layout.replay_game_control_button, parent, false
    )
) {
    private val iconView: AppCompatImageView =
        itemView.findViewById(R.id.replay_game_control_button_icon)

    fun bind(item: ReplayGameControlItem) {
        val drawable = ContextCompat.getDrawable(itemView.context, getIconResId(item))
        iconView.setImageDrawable(drawable)
        itemView.setOnClickListener { onClickListener(item) }
    }

    @DrawableRes
    private fun getIconResId(item: ReplayGameControlItem): Int {
        return when (item) {
            ReplayGameControlItem.Pause -> R.drawable.ic_pause_circle_outline_black_24dp
            ReplayGameControlItem.Play -> R.drawable.ic_play_circle_outline_black_24dp
        }
    }
}
