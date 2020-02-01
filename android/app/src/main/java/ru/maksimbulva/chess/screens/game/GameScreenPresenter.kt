package ru.maksimbulva.chess.screens.game

import ru.maksimbulva.chess.ChessEngineService
import ru.maksimbulva.chess.mvp.BasePresenter

class GameScreenPresenter(
    private val chessEngineService: ChessEngineService
) : BasePresenter<IGameScreenView, GameScreenViewModel>() {

    override fun onAttachedView(view: IGameScreenView) {
        super.onAttachedView(view)
        viewModel.position = chessEngineService.currentPosition
    }
}
