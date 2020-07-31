package ru.maksimbulva.chess.screens.game

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import org.koin.android.ext.android.get
import ru.maksimbulva.chess.R
import ru.maksimbulva.chess.core.engine.Player
import ru.maksimbulva.chess.mvp.BaseFragment
import ru.maksimbulva.ui.person.PersonPanelView
import ru.maksimbulva.ui.chessboard.ChessboardView
import ru.maksimbulva.ui.move_list.MoveListView
import ru.maksimbulva.ui.replay.ReplayGameControlsView

class GameScreenFragment(
    private val moveListItemsGenerator: MoveListItemsGenerator
) : BaseFragment<GameScreenPresenter, IGameScreenView, GameScreenViewModel, GameScreenAction>(
        R.layout.fragment_game_screen
    ), IGameScreenView
{
    private lateinit var chessboardView: ChessboardView
    private lateinit var personPanelViews: Array<PersonPanelView>
    private lateinit var moveListView: MoveListView
    private lateinit var replayGameControlsView: ReplayGameControlsView

    private val topPanelView: PersonPanelView
        get() = personPanelViews.first()

    private val bottomPanelView: PersonPanelView
        get() = personPanelViews.last()

    private lateinit var actionBarWrapper: GameScreenActionBarWrapper

    override val view: IGameScreenView = this

    override fun createPresenter() = GameScreenPresenter(get(), get(), get(), get())

    override fun obtainViewModel(): GameScreenViewModel {
        return ViewModelProvider(this).get(GameScreenViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        actionBarWrapper = GameScreenActionBarWrapper(context!!.resources, get()) {
            actionBarPresenter
        }

        viewModel.viewState.observe(this, Observer(this::showState))
    }

    override fun onViewCreated(view: View) {
        chessboardView = view.findViewById(R.id.chessboard)
        personPanelViews = arrayOf(
            view.findViewById(R.id.top_person_panel),
            view.findViewById(R.id.bottom_person_panel)
        )
        moveListView = view.findViewById(R.id.move_list)
        moveListView.setOnResizeButtonClicked {
            publishAction(GameScreenAction.MoveListResizeButtonClicked(it))
        }
        moveListView.onMoveItemClickListener = { moveItem, player ->
            publishAction(GameScreenAction.MoveListItemClicked(moveItem, player))
        }
        replayGameControlsView = view.findViewById(R.id.replay_game_controls)
        replayGameControlsView.onClickListener = {
            publishAction(GameScreenAction.ReplayControlButtonClicked(it))
        }
    }

    private fun showState(viewState: GameScreenViewModel.ViewState) {
        showChessboardState(viewState)
        showPlayerPanelsState(viewState)

        val lastMove = viewState.gameState.moveHistory.lastOrNull()
        actionBarWrapper.showState(viewState, lastMove)

        with (moveListView) {
            setCollapsed(viewState.moveListCollapsed)
            setItems(
                moveListItemsGenerator.generateMoveListItems(
                    context!!.resources,
                    viewState.gameState.moveHistory,
                    viewState.selectedHistoryMove
                )
            )
        }

        replayGameControlsView.setItems(viewState.replayControlItems)
    }

    private fun showChessboardState(viewState: GameScreenViewModel.ViewState) {
        with (viewState) {
            val boardItems = ChessboardItemsGenerator.generateForBoard(
                chessEngineState.position.board,
                playerOnTop
            )
            chessboardView.setItems(boardItems)
        }
    }

    private fun showPlayerPanelsState(viewState: GameScreenViewModel.ViewState) {
        Player.values().forEach {
            with (playerPanel(it, viewState.playerOnTop)) {
                val person = viewState.chessEngineState.players.get(it)
                setName(person.nameResId)
                setPortrait(person.portrait)
            }
        }
    }

    private fun playerPanel(player: Player, playerOnTop: Player): PersonPanelView {
        return if (playerOnTop == player) topPanelView else bottomPanelView
    }
}
