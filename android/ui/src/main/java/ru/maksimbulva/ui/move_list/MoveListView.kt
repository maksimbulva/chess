package ru.maksimbulva.ui.move_list

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.move_list.view.*
import ru.maksimbulva.chess.core.engine.Player
import ru.maksimbulva.ui.R
import ru.maksimbulva.ui.move_list.items.MoveListAdapter
import ru.maksimbulva.ui.move_list.items.MoveListItem

class MoveListView(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int)
    : FrameLayout(context, attributeSet, defStyleAttr) {

    var onMoveItemClickListener: ((MoveListItem, Player) -> Unit)? = null

    private val adapter = MoveListAdapter(context) { item, player ->
        onMoveItemClickListener?.invoke(item, player)
    }

    constructor(context: Context) : this(context, attributeSet = null, defStyleAttr = 0)
    constructor(context: Context, attributeSet: AttributeSet)
            : this(context, attributeSet, defStyleAttr = 0)

    init {
        View.inflate(context, R.layout.move_list, this)

        with (moves_recycler_view) {
            layoutManager = LinearLayoutManager(context)
            adapter = this@MoveListView.adapter
        }
    }

    fun setItems(items: List<MoveListItem>) {
        adapter.setItems(items)
        moves_recycler_view.scrollToPosition(items.lastIndex)
    }

    fun setOnExpandButtonClicked(listener: () -> Unit) {
        move_list_expand_button.setOnClickListener { listener() }
    }
}
