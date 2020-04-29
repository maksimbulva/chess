package ru.maksimbulva.ui.move_list.items

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.maksimbulva.ui.R

internal class MoveListItemViewHolder(context: Context, parent: ViewGroup)
    : RecyclerView.ViewHolder(
        LayoutInflater.from(context).inflate(R.layout.move_list_item, parent, false)
) {

    private val moveNumberText: TextView = itemView.findViewById(R.id.move_number_text)
    private val whiteMoveText: TextView = itemView.findViewById(R.id.white_move_text)
    private val blackMoveText: TextView = itemView.findViewById(R.id.black_move_text)

    fun bind(item: MoveListItem) {
        moveNumberText.text = item.moveNumberText
        whiteMoveText.text = item.whiteMoveText
        blackMoveText.text = item.blackMoveText
    }
}
