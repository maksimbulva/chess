package ru.maksimbulva.chess.screens.game_setup

import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ru.maksimbulva.chess.R
import ru.maksimbulva.chess.mvp.BaseFragment

class GameSetupScreenFragment(

) : BaseFragment<GameSetupScreenPresenter, IGameSetupScreenView, GameSetupScreenViewModel, GameSetupScreenAction>(
    R.layout.fragment_game_setup_screen
), IGameSetupScreenView {

    override val view: IGameSetupScreenView = this

    override fun createPresenter() = GameSetupScreenPresenter()

    override fun obtainViewModel(): GameSetupScreenViewModel {
        return ViewModelProvider(this).get(GameSetupScreenViewModel::class.java)
    }

    override fun onViewCreated(view: View) {
        view.setOnClickListener {
            findNavController().navigate(R.id.nav_acton_start_game)
        }
    }
}
