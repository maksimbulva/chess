package ru.maksimbulva.chess

import androidx.appcompat.app.AppCompatActivity
import ru.maksimbulva.chess.screens.game.GameScreenFragment

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    lateinit var actionBarPresenter: ActionBarPresenter

    override fun onStart() {
        super.onStart()
        actionBarPresenter = ActionBarPresenter(supportActionBar!!)

        supportFragmentManager
            .beginTransaction()
            .add(R.id.content, GameScreenFragment())
            .commit()
    }
}
