package ru.maksimbulva.chess.screens.game

import ru.maksimbulva.chess.chess.ChessEngineService
import ru.maksimbulva.chess.core.PlayerMap
import ru.maksimbulva.chess.core.engine.Player
import ru.maksimbulva.chess.mvp.BasePresenter
import ru.maksimbulva.chess.person.PersonsRepository
import ru.maksimbulva.chess.screens.game.GameScreenViewModel.ViewState

class GameScreenPresenter(
    private val chessEngineService: ChessEngineService,
    private val personsRepository: PersonsRepository,
    private val interactor: GameScreenInteractor
) : BasePresenter<IGameScreenView, GameScreenViewModel>() {

    override fun onCreate(viewModel: GameScreenViewModel) {
        super.onCreate(viewModel)

        viewModel.currentState = ViewState(
            position = chessEngineService.currentPosition,
            playerOnTop = Player.Black,
            playersState = PlayerMap(
                blackPlayerValue = GameScreenPersonState(),
                whitePlayerValue = GameScreenPersonState()
            )
        )

        chessEngineService.setPlayers(
            PlayerMap(
                blackPlayerValue = personsRepository.alice,
                whitePlayerValue = personsRepository.bob
            )
        )
    }

    override fun onAttachedView(view: IGameScreenView) {
        super.onAttachedView(view)

        addSubscription(
            chessEngineService.position.subscribe {
                viewModel.currentState = viewModel.currentState.copy(
                    position = it
                )
                interactor.onPositionChanged()
            }
        )

        addSubscription(
            chessEngineService.bestVariation.subscribe { bestVariation ->
                val currentPlayersState = viewModel.currentState.playersState
                viewModel.currentState = viewModel.currentState.copy(
                    playersState = PlayerMap(
                        blackPlayerValue = currentPlayersState.get(Player.Black).copy(
                            bestVariation = bestVariation.blackPlayerValue
                        ),
                        whitePlayerValue = currentPlayersState.get(Player.White).copy(
                            bestVariation = bestVariation.whitePlayerValue
                        )
                    )
                )
            }
        )
    }

    override fun onDetachedView() {
        super.onDetachedView()
        interactor.stop()
    }
}
