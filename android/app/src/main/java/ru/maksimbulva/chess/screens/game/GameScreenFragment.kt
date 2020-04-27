package ru.maksimbulva.chess.screens.game

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_game_screen.*
import org.koin.android.ext.android.get
import ru.maksimbulva.chess.R
import ru.maksimbulva.chess.core.engine.Player
import ru.maksimbulva.chess.mvp.BaseFragment
import ru.maksimbulva.ui.person.PersonPanelView
import ru.maksimbulva.ui.chessboard.ChessboardView

class GameScreenFragment(
    private val moveListItemsGenerator: MoveListItemsGenerator
) : BaseFragment<GameScreenPresenter, IGameScreenView, GameScreenViewModel>(
        R.layout.fragment_game_screen
    ), IGameScreenView
{
    private lateinit var chessboardView: ChessboardView
    private lateinit var personPanelViews: Array<PersonPanelView>

    private val topPanelView: PersonPanelView
        get() = personPanelViews.first()

    private val bottomPanelView: PersonPanelView
        get() = personPanelViews.last()

    private lateinit var actionBarWrapper: GameScreenActionBarWrapper

    override val view: IGameScreenView = this

    override fun createPresenter() = GameScreenPresenter(get(), get(), get())

    override fun obtainViewModel(): GameScreenViewModel {
        return ViewModelProvider(this).get(GameScreenViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        actionBarWrapper = GameScreenActionBarWrapper(context!!.resources, get()) {
            actionBarPresenter
        }

        viewModel.viewState.observe(this, Observer<GameScreenViewModel.ViewState>
            { viewState ->
                chessboardView.setItems(viewState.boardItems)

                Player.values().forEach {
                    val playerState = viewState.playersState.get(it)
                    playerPanel(it, viewState.playerOnTop).setState(playerState)
                }

                val lastMove = viewState.moveHistory.lastOrNull()
                actionBarWrapper.showState(viewState, lastMove)

                move_list.setItems(
                    moveListItemsGenerator.generateMoveListItems(
                        context!!.resources,
                        viewState.moveHistory
                    )
                )
            }
        )
    }

    override fun onViewCreated(view: View) {
        chessboardView = view.findViewById(R.id.chessboard)
        personPanelViews = arrayOf(
            view.findViewById(R.id.top_person_panel),
            view.findViewById(R.id.bottom_person_panel)
        )
    }

    private fun playerPanel(player: Player, playerOnTop: Player): PersonPanelView {
        return if (playerOnTop == player) topPanelView else bottomPanelView
    }
}
