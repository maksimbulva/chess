package ru.maksimbulva.chess.screens.game

import io.reactivex.Flowable
import ru.maksimbulva.chess.chess.ChessEngineService
import ru.maksimbulva.chess.core.PlayerMap
import ru.maksimbulva.chess.core.engine.Player
import ru.maksimbulva.chess.mvp.BasePresenter
import ru.maksimbulva.chess.person.PersonsRepository
import ru.maksimbulva.chess.screens.game.GameScreenViewModel.ViewState
import ru.maksimbulva.ui.person.PersonPanelState

class GameScreenPresenter(
    private val chessEngineService: ChessEngineService,
    private val personsRepository: PersonsRepository,
    private val interactor: GameScreenInteractor,
    private val replayControlsInteractor: GameScreenReplayControlsInteractor
) : BasePresenter<IGameScreenView, GameScreenViewModel, GameScreenAction>() {

    override fun onCreate(viewModel: GameScreenViewModel) {
        super.onCreate(viewModel)

        chessEngineService.setPlayers(
            PlayerMap(
                blackPlayerValue = personsRepository.alice,
                whitePlayerValue = personsRepository.bob
            )
        )

        replayControlsInteractor.resetControls()

        viewModel.currentState = ViewState(
            position = chessEngineService.currentPosition,
            moveHistory = chessEngineService.moveHistory,
            adjudicationResult = chessEngineService.adjudicateGame(),
            playerOnTop = Player.Black,
            playersState = PlayerMap(
                blackPlayerValue = PersonPanelState(
                    portraitResId = chessEngineService.person(Player.Black).portrait,
                    nameResId = chessEngineService.person(Player.Black).nameResId
                ),
                whitePlayerValue = PersonPanelState(
                    portraitResId = chessEngineService.person(Player.White).portrait,
                    nameResId = chessEngineService.person(Player.White).nameResId
                )
            ),
            moveListCollapsed = true,
            replayControlItems = replayControlsInteractor.currentReplayControls
        )
    }

    override fun onAttachedView(view: IGameScreenView) {
        super.onAttachedView(view)

        addSubscription(
            chessEngineService.position.subscribe {
                val adjudicationResult = chessEngineService.adjudicateGame()
                viewModel.currentState = viewModel.currentState.copy(
                    position = it,
                    moveHistory = chessEngineService.moveHistory,
                    adjudicationResult = adjudicationResult,
                    replayControlItems = replayControlsInteractor.currentReplayControls
                )
                interactor.onPositionChanged(adjudicationResult)
            }
        )

        addSubscription(
            chessEngineService.bestVariation.subscribe { bestVariation ->
                val currentPlayersState = viewModel.currentState.playersState
                viewModel.currentState = viewModel.currentState.copy(
                    playersState = PlayerMap(
                        blackPlayerValue = currentPlayersState.get(Player.Black).copy(
                            evaluation = bestVariation.blackPlayerValue?.evaluation?.toDouble()
                        ),
                        whitePlayerValue = currentPlayersState.get(Player.White).copy(
                            evaluation = bestVariation.whitePlayerValue?.evaluation?.toDouble()
                        )
                    )
                )
            }
        )

        addSubscription(
            replayControlsInteractor.replayControls.subscribe { replayControlItems ->
                viewModel.currentState = viewModel.currentState.copy(
                    replayControlItems = replayControlItems
                )
            }
        )

        addSubscription(
            interactor.engineState.subscribe(replayControlsInteractor::onEngineStateUpdate)
        )
    }

    override fun onDetachedView() {
        super.onDetachedView()
        interactor.stop()
    }

    override fun onActionReceived(action: GameScreenAction) {
        when (action) {
            GameScreenAction.ExpandMoveListClicked -> onExpandMoveListClicked()
            is GameScreenAction.ReplayControlButtonClicked -> {
                replayControlsInteractor.onReplayControlButtonClicked(action.item)
            }
        }
    }

    private fun onExpandMoveListClicked() {
        val currentViewState = viewModel.currentState
        viewModel.currentState = currentViewState.copy(
            moveListCollapsed = !currentViewState.moveListCollapsed
        )
    }
}
