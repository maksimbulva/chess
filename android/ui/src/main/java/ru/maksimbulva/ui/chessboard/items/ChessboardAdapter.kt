package ru.maksimbulva.ui.chessboard.items

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import ru.maksimbulva.ui.chessboard.ChessboardSquareView
import ru.maksimbulva.ui.chessboard.ChessboardTheme

internal class ChessboardAdapter(
    private val context: Context,
    private val theme: ChessboardTheme
) : BaseAdapter() {

    private var items: List<ChessboardItem> = emptyList()

    override fun getCount() = items.size

    override fun getItemId(index: Int) = index.toLong()

    override fun getItem(index: Int) = items.getOrNull(index)

    override fun getView(index: Int, convertView: View?, parent: ViewGroup?): View {
        val itemView = if (convertView != null && convertView is ChessboardSquareView) {
            convertView
        } else {
            ChessboardSquareView(context)
        }

        return itemView.also {
            it.setTheme(theme)
            it.bind(items[index])
        }
    }

    fun setItems(items: List<ChessboardItem>) {
        this.items = items.toList()
        notifyDataSetChanged()
    }
}
