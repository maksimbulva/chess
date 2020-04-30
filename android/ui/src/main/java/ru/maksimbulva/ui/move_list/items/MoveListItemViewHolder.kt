package ru.maksimbulva.ui.move_list.items

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ru.maksimbulva.chess.core.engine.Player
import ru.maksimbulva.ui.R

internal class MoveListItemViewHolder(
    context: Context,
    parent: ViewGroup,
    private val onClickListener: (MoveListItem, Player) -> Unit
) : RecyclerView.ViewHolder(
        LayoutInflater.from(context).inflate(R.layout.move_list_item, parent, false)
) {

    private val moveNumberText: TextView = itemView.findViewById(R.id.move_number_text)
    private val whiteMoveText: TextView = itemView.findViewById(R.id.white_move_text)
    private val blackMoveText: TextView = itemView.findViewById(R.id.black_move_text)

    fun bind(item: MoveListItem) {
        moveNumberText.text = item.moveNumberText
        Player.values().forEach { player ->
            val textView = getTextViewByPlayer(player)
            textView.text = item.getMoveItem(player)?.moveText
            setSelected(item.selectedPlayerMove == player, textView)
            textView.setOnClickListener { onClickListener(item, player) }
        }
    }

    private fun setSelected(value: Boolean, moveText: TextView) {
        moveText.background = getBackgroundDrawable(value)
    }

    private fun getBackgroundDrawable(isSelected: Boolean): Drawable? {
        return if (isSelected) {
            ContextCompat.getDrawable(itemView.context, R.drawable.selected_move_item_background)
        } else {
            null
        }
    }

    private fun getTextViewByPlayer(player: Player): TextView {
        return when (player) {
            Player.Black -> blackMoveText
            Player.White -> whiteMoveText
        }
    }
}
