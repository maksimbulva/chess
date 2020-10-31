package ru.maksimbulva.chess.screens.game_setup

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.core.view.isInvisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.android.ext.android.getKoin
import ru.maksimbulva.chess.R
import ru.maksimbulva.chess.core.engine.Player
import ru.maksimbulva.chess.mvp.BaseFragment
import ru.maksimbulva.chess.person.convertToPersonCardItem
import ru.maksimbulva.chess.screens.game_setup.GameSetupScreenAction.*
import ru.maksimbulva.chess.screens.game_setup.GameSetupScreenViewModel.ViewState
import ru.maksimbulva.chess.screens.game_setup.person_list.PersonListAdapter
import ru.maksimbulva.ui.person.PersonCardItem

class GameSetupScreenFragment
: BaseFragment<GameSetupScreenPresenter, IGameSetupScreenView, GameSetupScreenViewModel, GameSetupScreenAction>(
    R.layout.fragment_game_setup_screen
), IGameSetupScreenView {

    private lateinit var playerCardViews: Map<Player, PlayerSetupCardView>

    private lateinit var personListView: RecyclerView
    private val personListAdapter = PersonListAdapter(onClick = ::onPersonItemClicked)

    override val view: IGameSetupScreenView = this

    override fun createPresenter() = GameSetupScreenPresenter(
        personsRepository = getKoin().get(),
        startGame = ::goToGame
    )

    override fun obtainViewModel(): GameSetupScreenViewModel {
        return ViewModelProvider(this).get(GameSetupScreenViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.viewState.observe(this, Observer(this::showState))
    }

    override fun onViewCreated(view: View) {
        playerCardViews = mapOf(
            Player.Black to view.findViewById(R.id.game_setup_player_black),
            Player.White to view.findViewById(R.id.game_setup_player_white)
        )

        playerCardViews.forEach { (player, playerCardView) ->
            playerCardView.setOnReplacePlayerClickListener {
                publishAction(ReplacePlayerButtonClicked(player))
            }
        }

        view.findViewById<Button>(R.id.start_game_button).setOnClickListener {
            publishAction(StartGameButtonClicked)
        }

        personListView = view.findViewById(R.id.person_list)
        with (personListView) {
            layoutManager = LinearLayoutManager(context)
            adapter = personListAdapter
        }
    }

    private fun showState(viewState: ViewState) {
        personListView.isInvisible = viewState !is ViewState.PersonList

        when (viewState) {
            is ViewState.GameLobby -> showLobbyState(viewState)
            is ViewState.PersonList -> showPersonList(viewState)
        }
    }

    private fun showLobbyState(viewState: ViewState.GameLobby) {
        playerCardViews.forEach { (player, playerCardView) ->
            val person = viewState.players.get(player)
            playerCardView.setPerson(person)
        }
    }

    private fun showPersonList(viewState: ViewState.PersonList) {
        val personItems = viewState.persons.map {
            convertToPersonCardItem(requireContext().resources, it)
        }
        personListAdapter.setItems(personItems)
    }

    private fun onPersonItemClicked(item: PersonCardItem) {
        publishAction(SelectPerson(item.personId))
    }

    private fun goToGame() {
        findNavController().navigate(R.id.nav_acton_start_game)
    }
}
