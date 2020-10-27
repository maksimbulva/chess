package ru.maksimbulva.chess.screens

import androidx.fragment.app.Fragment
import org.koin.core.KoinComponent
import ru.maksimbulva.chess.screens.game.GameScreenFragment

class FragmentFactory : KoinComponent {

    fun create(screen: Screen): Fragment {
        return when (screen) {
            Screen.Game -> GameScreenFragment(getKoin().get())
            Screen.GameSetup -> throw NotImplementedError()
        }
    }
}
