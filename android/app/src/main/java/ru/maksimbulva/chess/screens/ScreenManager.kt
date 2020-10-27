package ru.maksimbulva.chess.screens

import androidx.fragment.app.FragmentManager
import ru.maksimbulva.chess.R

class ScreenManager(
    private val supportFragmentManager: FragmentManager,
    private val fragmentFactory: FragmentFactory
) {

    fun show(screen: Screen) {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.content, fragmentFactory.create(screen))
            .commit()
    }
}
