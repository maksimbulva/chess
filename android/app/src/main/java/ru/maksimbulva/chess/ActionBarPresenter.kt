package ru.maksimbulva.chess

import androidx.appcompat.app.ActionBar

class ActionBarPresenter(private val actionBar: ActionBar) {

    fun setTitle(title: String?) {
        actionBar.title = title
    }
}
