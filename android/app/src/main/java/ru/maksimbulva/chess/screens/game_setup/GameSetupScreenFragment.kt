package ru.maksimbulva.chess.screens.game_setup

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.lifecycle.Observer
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.viewState.observe(this, Observer(this::showState))
    }

    override fun onViewCreated(view: View) {
        view.findViewById<Button>(R.id.start_game_button).setOnClickListener {
            publishAction(GameSetupScreenAction.StartGameButtonClicked)
        }
    }

    private fun showState(viewState: GameSetupScreenViewModel.ViewState) {
        if (viewState.shouldStartGame) {
            goToGame()
        }
    }

    private fun goToGame() {
        findNavController().navigate(R.id.nav_acton_start_game)
    }
}
