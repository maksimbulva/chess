package ru.maksimbulva.chess.screens.game

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import org.koin.android.ext.android.get
import ru.maksimbulva.chess.R
import ru.maksimbulva.chess.chess.GameAdjudicationResult
import ru.maksimbulva.chess.core.engine.Player
import ru.maksimbulva.chess.mvp.BaseFragment
import ru.maksimbulva.ui.person.PersonPanel
import ru.maksimbulva.ui.chessboard.ChessboardView

class GameScreenFragment
    : BaseFragment<GameScreenPresenter, IGameScreenView, GameScreenViewModel>(
        R.layout.fragment_game_screen
    ), IGameScreenView
{
    private lateinit var chessboardView: ChessboardView
    private lateinit var personPanels: Array<PersonPanel>

    private val topPanel: PersonPanel
        get() = personPanels.first()

    private val bottomPanel: PersonPanel
        get() = personPanels.last()

    override val view: IGameScreenView = this

    override fun createPresenter() = GameScreenPresenter(get(), get(), get())

    override fun obtainViewModel(): GameScreenViewModel {
        return ViewModelProvider(this).get(GameScreenViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.viewState.observe(this, Observer<GameScreenViewModel.ViewState>
            { viewState ->
                chessboardView.setItems(viewState.boardItems)

                Player.values().forEach {
                    val playerState = viewState.playersState.get(it)
                    playerPanel(it, viewState.playerOnTop).setState(playerState)
                }

                actionBarPresenter?.setTitle(generateActionBarTitle(viewState))
            }
        )
    }

    override fun onViewCreated(view: View) {
        chessboardView = view.findViewById(R.id.chessboard)
        personPanels = arrayOf(
            view.findViewById(R.id.top_person_panel),
            view.findViewById(R.id.bottom_person_panel)
        )
    }

    private fun generateActionBarTitle(viewState: GameScreenViewModel.ViewState): String? {
        return when (val adjudicationResult = viewState.adjudicationResult) {
            is GameAdjudicationResult.Win -> {
                when (adjudicationResult.reason) {
                    GameAdjudicationResult.Win.WinReason.Checkmate -> {
                        context?.getString(when (adjudicationResult.winner) {
                            Player.Black -> R.string.action_bar_white_checkmated
                            Player.White -> R.string.action_bar_black_checkmated
                        })
                    }
                    else -> null
                }
            }
            else -> null
        }
    }

    private fun playerPanel(player: Player, playerOnTop: Player): PersonPanel {
        return if (playerOnTop == player) topPanel else bottomPanel
    }
}
