package ru.maksimbulva.chess.screens.game

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import org.koin.android.ext.android.get
import ru.maksimbulva.chess.R
import ru.maksimbulva.chess.mvp.BaseFragment
import ru.maksimbulva.ui.PersonPanel
import ru.maksimbulva.ui.chessboard.ChessboardView

class GameScreenFragment
    : BaseFragment<GameScreenPresenter, IGameScreenView, GameScreenViewModel>(
        R.layout.fragment_game_screen
    ), IGameScreenView
{
    private lateinit var chessboardView: ChessboardView
    private lateinit var personPanels: Array<PersonPanel>

    override val view: IGameScreenView = this

    override fun createPresenter() = GameScreenPresenter(get(), get(), get())

    override fun obtainViewModel(): GameScreenViewModel {
        return ViewModelProvider(this).get(GameScreenViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.viewState.observe(this, Observer<GameScreenViewModel.ViewState>{
            viewState ->
                chessboardView.setItems(viewState.boardItems)
            /*
            it[Player.Black]?.bestVariation?.let { variation ->
                topPersonPanel.updateEvaluation(PersonPanel.EvaluationModel(
                    evaluation = variation.evaluation.toDouble()
                ))
            }
             */
        })
    }

    override fun onViewCreated(view: View) {
        chessboardView = view.findViewById(R.id.chessboard)
        personPanels = arrayOf(
            view.findViewById(R.id.top_person_panel),
            view.findViewById(R.id.bottom_person_panel)
        )
    }
}
