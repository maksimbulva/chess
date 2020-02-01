package ru.maksimbulva.chess.screens.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import org.koin.android.ext.android.get
import ru.maksimbulva.chess.R
import ru.maksimbulva.chess.mvp.BaseFragment
import ru.maksimbulva.ui.chessboard.ChessboardView
import ru.maksimbulva.ui.chessboard.items.ChessboardItem

class GameScreenFragment
    : BaseFragment<GameScreenPresenter, IGameScreenView, GameScreenViewModel>(
        R.layout.fragment_game_screen
    ), IGameScreenView
{
    private lateinit var chessboardView: ChessboardView
    private lateinit var playMoveButton: Button

    override val view: IGameScreenView = this

    override fun createPresenter() = GameScreenPresenter(get())

    override fun obtainViewModel(): GameScreenViewModel {
        return ViewModelProvider(this).get(GameScreenViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.chessboarItems.observe(this, Observer<List<ChessboardItem>>{ items ->
            chessboardView.setItems(items)
        })
    }

    override fun onViewCreated(view: View) {
        chessboardView = view.findViewById(R.id.chessboard)
        playMoveButton = view.findViewById(R.id.play_move_button)
    }

    override fun setPlayMoveOnClickListener(listener: () -> Unit) {
        playMoveButton.setOnClickListener { listener() }
    }
}
