package ru.maksimbulva.chess.screens.game

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import org.koin.android.ext.android.get
import ru.maksimbulva.chess.R
import ru.maksimbulva.chess.core.engine.Player
import ru.maksimbulva.chess.mvp.BaseFragment
import ru.maksimbulva.ui.PersonPanel
import ru.maksimbulva.ui.chessboard.ChessboardView
import ru.maksimbulva.ui.chessboard.items.ChessboardItem

class GameScreenFragment
    : BaseFragment<GameScreenPresenter, IGameScreenView, GameScreenViewModel>(
        R.layout.fragment_game_screen
    ), IGameScreenView
{
    private lateinit var chessboardView: ChessboardView
    private lateinit var topPersonPanel: PersonPanel
    private lateinit var playMoveButton: Button

    override val view: IGameScreenView = this

    override fun createPresenter() = GameScreenPresenter(get(), get(), get())

    override fun obtainViewModel(): GameScreenViewModel {
        return ViewModelProvider(this).get(GameScreenViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.boardState.observe(this, Observer<GameScreenBoardState>{ boardState ->
            chessboardView.setItems(boardState.chessboardItems)
        })

        viewModel.personsState.observe(this, Observer<Map<Player, GameScreenPersonState>>{
            it[Player.Black]?.bestVariation?.let { variation ->
                topPersonPanel.updateEvaluation(PersonPanel.EvaluationModel(
                    evaluation = variation.evaluation.toDouble()
                ))
            }
        })
    }

    override fun onViewCreated(view: View) {
        chessboardView = view.findViewById(R.id.chessboard)
        topPersonPanel = view.findViewById(R.id.top_person_panel)
        playMoveButton = view.findViewById(R.id.play_move_button)

        topPersonPanel.setPortrait(R.drawable.portrait_001)
    }

    override fun setPlayMoveOnClickListener(listener: () -> Unit) {
        playMoveButton.setOnClickListener { listener() }
    }
}
