package ru.maksimbulva.ui.common

import androidx.recyclerview.widget.RecyclerView

abstract class SimpleAdapter<VH, I>
    : RecyclerView.Adapter<VH>() where VH : RecyclerView.ViewHolder {

    private var items = emptyList<I>()

    override fun getItemCount() = items.size

    fun setItems(items: List<I>) {
        if (items != this.items) {
            this.items = items
            notifyDataSetChanged()
        }
    }

    protected fun getItem(index: Int) = items[index]
}
