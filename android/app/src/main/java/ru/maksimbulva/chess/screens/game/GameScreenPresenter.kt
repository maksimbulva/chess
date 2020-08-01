package ru.maksimbulva.chess.screens.game

import ru.maksimbulva.chess.chess.ChessEngineService
import ru.maksimbulva.chess.core.PlayerMap
import ru.maksimbulva.chess.core.engine.Player
import ru.maksimbulva.chess.core.engine.move.DetailedMove
import ru.maksimbulva.chess.core.engine.position.Position
import ru.maksimbulva.chess.mvp.BasePresenter
import ru.maksimbulva.chess.person.PersonsRepository
import ru.maksimbulva.chess.screens.game.GameScreenViewModel.ViewState
import ru.maksimbulva.ui.move_list.items.MoveListItem

class GameScreenPresenter(
    private val chessEngineService: ChessEngineService,
    private val personsRepository: PersonsRepository,
    private val interactor: GameScreenInteractor,
    private val replayControlsInteractor: GameScreenReplayControlsInteractor
) : BasePresenter<IGameScreenView, GameScreenViewModel, GameScreenAction>() {

    private val currentState: ViewState
        get() = viewModel.currentState

    private val gameModeUseCase = GameModeUseCase(chessEngineService)

    override fun onCreate(viewModel: GameScreenViewModel) {
        super.onCreate(viewModel)

        chessEngineService.players = PlayerMap(
            blackPlayerValue = personsRepository.alice,
            whitePlayerValue = personsRepository.bob
        )

        replayControlsInteractor.resetControls()

        viewModel.currentState = ViewState(
            gameState = GameState(
                adjudicationResult = chessEngineService.adjudicateGame()
            ),
            chessEngineState = chessEngineService.currentState,
            selectedHistoryMove = null,
            playerOnTop = Player.Black,
            moveListCollapsed = true,
            replayControlItems = replayControlsInteractor.currentReplayControls
        )
    }

    override fun onAttachedView(view: IGameScreenView) {
        super.onAttachedView(view)
        interactor.start()
        chessEngineService.start()
        gameModeUseCase.start()

        addSubscription(
            chessEngineService.engineState.subscribe { engineState ->
                viewModel.currentState = currentState.copy(chessEngineState = engineState)
            }
        )

//        addSubscription(
//            chessEngineService.bestVariation.subscribe { bestVariation ->
//                val currentPlayersState = currentState.playersState
//                viewModel.currentState = currentState.copy(
//                    playersState = PlayerMap(
//                        blackPlayerValue = currentPlayersState.get(Player.Black).copy(
//                            evaluation = bestVariation.blackPlayerValue?.evaluation?.toDouble()
//                        ),
//                        whitePlayerValue = currentPlayersState.get(Player.White).copy(
//                            evaluation = bestVariation.whitePlayerValue?.evaluation?.toDouble()
//                        )
//                    )
//                )
//            }
//        )

        addSubscription(
            replayControlsInteractor.replayControls.subscribe { replayControlItems ->
                viewModel.currentState = currentState.copy(
                    replayControlItems = replayControlItems
                )
            }
        )

//        addSubscription(
//            interactor.engineState.subscribe(replayControlsInteractor::onEngineStateUpdate)
//        )
    }

    override fun onDetachedView() {
        gameModeUseCase.stop()
        chessEngineService.stop()
        interactor.stop()
        super.onDetachedView()
    }

    override fun onActionReceived(action: GameScreenAction) {
        when (action) {
            is GameScreenAction.MoveListResizeButtonClicked -> {
                onExpandMoveListClicked(action.expand)
            }
            is GameScreenAction.ReplayControlButtonClicked -> {
                replayControlsInteractor.onReplayControlButtonClicked(action.item)
            }
            is GameScreenAction.MoveListItemClicked -> {
                onMoveHistoryItemClicked(action.item, action.player)
            }
        }
    }

    private fun onExpandMoveListClicked(expand: Boolean) {
        val currentState = currentState
        viewModel.currentState = currentState.copy(
            moveListCollapsed = !expand
        )
    }

    private fun onMoveHistoryItemClicked(item: MoveListItem, player: Player) {
        val selectedHistoryMove = item.getMoveItem(player)?.detailedMove
        viewModel.currentState = currentState.copy(
            selectedHistoryMove = selectedHistoryMove
        )
        if (selectedHistoryMove != null) {
            interactor.goBackToMove(selectedHistoryMove, currentState.chessEngineState.moveHistory)
        }
    }

    private fun onPositionUpdated(position: Position) {
        val rewriteMoveHistory = shouldRewriteMoveHistory()
        val moveHistory = if (rewriteMoveHistory) {
            chessEngineService.moveHistory
        } else {
            currentState.chessEngineState.moveHistory
        }
        val adjudicationResult = if (rewriteMoveHistory) {
            chessEngineService.adjudicateGame()
        } else {
            currentState.gameState.adjudicationResult
        }
        viewModel.currentState = viewModel.currentState.copy(
            gameState = viewModel.currentState.gameState.copy(
                adjudicationResult = adjudicationResult
            ),
            chessEngineState = chessEngineService.currentState,
            replayControlItems = replayControlsInteractor.currentReplayControls
        )
        interactor.onPositionChanged(adjudicationResult)
    }

    private fun List<DetailedMove>.isSubsequenceOf(other: List<DetailedMove>): Boolean {
        if (size > other.size) {
            return false
        }
        for (i in indices) {
            if (this[i] != other[i]) {
                return false
            }
        }
        return true
    }

    private fun shouldRewriteMoveHistory(): Boolean {
        val currentMoveHistory = currentState.chessEngineState.moveHistory
        return currentMoveHistory.isSubsequenceOf(chessEngineService.moveHistory)
    }
}
