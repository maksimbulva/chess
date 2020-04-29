package ru.maksimbulva.ui.replay

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.maksimbulva.ui.R
import ru.maksimbulva.ui.replay.items.ReplayGameControlItem
import ru.maksimbulva.ui.replay.items.ReplayGameControlsAdapter

class ReplayGameControlsView(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int)
    : FrameLayout(context, attributeSet, defStyleAttr) {

    var onClickListener: ((ReplayGameControlItem) -> Unit)? = null

    private val controlsList: RecyclerView
    private val controlsListAdapter = ReplayGameControlsAdapter(context) {
        onClickListener?.invoke(it)
    }

    constructor(context: Context) : this(context, attributeSet = null, defStyleAttr = 0)
    constructor(context: Context, attributeSet: AttributeSet)
            : this(context, attributeSet, defStyleAttr = 0)

    init {
        View.inflate(context, R.layout.replay_game_controls, this)

        controlsList = findViewById(R.id.replay_game_controls_list)
        with (controlsList) {
            layoutManager = LinearLayoutManager(context)
            adapter = controlsListAdapter
        }
    }

    fun setItems(items: List<ReplayGameControlItem>) {
        controlsListAdapter.setItems(items)
    }
}
