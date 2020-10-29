package ru.maksimbulva.chess.screens.game_setup

import ru.maksimbulva.chess.mvp.BasePresenter

class GameSetupScreenPresenter(

) : BasePresenter<IGameSetupScreenView, GameSetupScreenViewModel, GameSetupScreenAction>() {

    private val currentState: GameSetupScreenViewModel.ViewState
        get() = viewModel.currentState

    override fun onCreate(viewModel: GameSetupScreenViewModel) {
        super.onCreate(viewModel)

        viewModel.currentState = GameSetupScreenViewModel.ViewState(
            shouldStartGame = false
        )
    }

    override fun onActionReceived(action: GameSetupScreenAction) {
        when (action) {
            GameSetupScreenAction.StartGameButtonClicked -> {
                onStartGameButtonClicked()
            }
        }
    }

    private fun onStartGameButtonClicked() {
        val currentState = currentState
        viewModel.currentState = currentState.copy(shouldStartGame = true)
    }
}
