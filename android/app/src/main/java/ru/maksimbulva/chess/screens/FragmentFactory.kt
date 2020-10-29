package ru.maksimbulva.chess.screens

import androidx.fragment.app.Fragment
import ru.maksimbulva.chess.screens.game.GameScreenFragment
import ru.maksimbulva.chess.screens.game_setup.GameSetupScreenFragment

class FragmentFactory {

    fun create(screen: Screen): Fragment {
        return when (screen) {
            Screen.Game -> GameScreenFragment()
            Screen.GameSetup -> GameSetupScreenFragment()
        }
    }
}
