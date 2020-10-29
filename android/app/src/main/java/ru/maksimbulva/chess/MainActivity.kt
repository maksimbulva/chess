package ru.maksimbulva.chess

import androidx.appcompat.app.AppCompatActivity
import ru.maksimbulva.chess.screens.FragmentFactory
import ru.maksimbulva.chess.screens.ScreenManager

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    lateinit var actionBarPresenter: ActionBarPresenter
    private lateinit var screenManager: ScreenManager

    override fun onStart() {
        if (!::actionBarPresenter.isInitialized) {
            actionBarPresenter = ActionBarPresenter(supportActionBar!!)
        }

        if (!::screenManager.isInitialized) {
            screenManager = ScreenManager(supportFragmentManager, FragmentFactory())
        }

        super.onStart()
//        screenManager.show(Screen.GameSetup)
    }
}
