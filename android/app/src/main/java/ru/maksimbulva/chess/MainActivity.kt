package ru.maksimbulva.chess

import androidx.appcompat.app.AppCompatActivity
import org.koin.android.ext.android.get
import ru.maksimbulva.chess.screens.game.GameScreenFragment

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    lateinit var actionBarPresenter: ActionBarPresenter

    override fun onStart() {
        super.onStart()
        actionBarPresenter = ActionBarPresenter(supportActionBar!!)

        supportFragmentManager
            .beginTransaction()
            .add(R.id.content, GameScreenFragment(get()))
            .commit()
    }
}
