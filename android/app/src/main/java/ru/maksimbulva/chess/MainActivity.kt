package ru.maksimbulva.chess

import androidx.appcompat.app.AppCompatActivity
import ru.maksimbulva.chess.screens.FragmentFactory
import ru.maksimbulva.chess.screens.Screen
import ru.maksimbulva.chess.screens.ScreenManager

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    lateinit var actionBarPresenter: ActionBarPresenter
    private lateinit var screenManager: ScreenManager

    override fun onStart() {
        super.onStart()
        actionBarPresenter = ActionBarPresenter(supportActionBar!!)

        if (!::screenManager.isInitialized) {
            screenManager = ScreenManager(supportFragmentManager, FragmentFactory())
        }

        screenManager.show(Screen.GameSetup)
    }
}
