package ru.maksimbulva.chess.screens.game_setup

import ru.maksimbulva.chess.core.PlayerMap
import ru.maksimbulva.chess.core.engine.Player
import ru.maksimbulva.chess.mvp.BasePresenter
import ru.maksimbulva.chess.person.Person
import ru.maksimbulva.chess.person.PersonsRepository
import ru.maksimbulva.chess.screens.game_setup.GameSetupScreenAction.*
import ru.maksimbulva.chess.screens.game_setup.GameSetupScreenViewModel.ViewState

class GameSetupScreenPresenter(
    private val personsRepository: PersonsRepository,
    private val startGame: (PlayerMap<Person?>) -> Unit
) : BasePresenter<IGameSetupScreenView, GameSetupScreenViewModel, GameSetupScreenAction>() {

    private val currentState: ViewState
        get() = viewModel.currentState

    override fun onCreate(viewModel: GameSetupScreenViewModel) {
        super.onCreate(viewModel)

        viewModel.currentState = ViewState.GameLobby(
            players = PlayerMap(
                blackPlayerValue = null,
                whitePlayerValue = null
            )
        )
    }

    override fun onActionReceived(action: GameSetupScreenAction) {
        when (action) {
            is ReplacePlayerButtonClicked -> onReplacePlayerButtonClicked(action.player)
            is SelectPerson -> onPersonSelected(action.personId)
            StartGameButtonClicked -> onStartGameButtonClicked()
        }
    }

    private fun onReplacePlayerButtonClicked(player: Player) {
        viewModel.currentState = ViewState.PersonList(
            players = currentState.players,
            playerToSelect = player,
            persons = personsRepository.getAllPersons()
        )
    }

    private fun onPersonSelected(personId: Int?) {
        val selectedPerson = personId?.let { personsRepository.findPerson(it) } ?: return
        val currentState = currentState as? ViewState.PersonList ?: return
        viewModel.currentState = ViewState.GameLobby(
            players = currentState.players.copyWith(
                currentState.playerToSelect,
                selectedPerson
            )
        )
    }

    private fun onStartGameButtonClicked() {
        startGame(currentState.players)
    }
}
