package ru.maksimbulva.chess.screens.game

import ru.maksimbulva.chess.chess.ChessEngineService
import ru.maksimbulva.chess.core.PlayerMap
import ru.maksimbulva.chess.core.engine.Player
import ru.maksimbulva.chess.mvp.BasePresenter
import ru.maksimbulva.chess.person.Person
import ru.maksimbulva.chess.person.PersonsRepository
import ru.maksimbulva.chess.screens.game.GameScreenViewModel.ViewState
import ru.maksimbulva.ui.move_list.items.MoveListItem
import ru.maksimbulva.ui.replay.items.ReplayGameControlItem

class GameScreenPresenter(
    private val chessEngineService: ChessEngineService,
    private val personsRepository: PersonsRepository
) : BasePresenter<IGameScreenView, GameScreenViewModel, GameScreenAction>() {

    private val currentState: ViewState
        get() = viewModel.currentState

    private val gameModeUseCase = GameModeUseCase(chessEngineService)
    private val analysisModeUseCase = AnalysisModeUseCase()

    private lateinit var playerIds: PlayerMap<Int>
    private var isInAnalysisMode = false

    override fun onCreate(viewModel: GameScreenViewModel) {
        super.onCreate(viewModel)

        chessEngineService.players = PlayerMap(
            blackPlayerValue = getPerson(Player.Black),
            whitePlayerValue = getPerson(Player.White)
        )

        viewModel.currentState = ViewState(
            gameState = GameState(
                adjudicationResult = chessEngineService.adjudicateGame()
            ),
            chessEngineState = chessEngineService.currentState,
            selectedHistoryMove = null,
            playerOnTop = Player.Black,
            moveListCollapsed = true,
            replayControlItems = getBottomBarButtons()
        )
    }

    override fun onAttachedView(view: IGameScreenView) {
        super.onAttachedView(view)

        chessEngineService.start()
        gameModeUseCase.start()

        addSubscription(
            chessEngineService.engineState.subscribe { engineState ->
                android.util.Log.w("TXL", "Set game mode position")
                viewModel.currentState = currentState.copy(chessEngineState = engineState)
            }
        )

        addSubscription(
            analysisModeUseCase.analysisPosition.subscribe { position ->
                if (isInAnalysisMode) {
                    android.util.Log.w("TXL", "Set analysis mode position")
                    viewModel.currentState = currentState.copy(
                        chessEngineState = currentState.chessEngineState.copy(
                            position = position,
                            bestVariation = null
                        ),
                        selectedHistoryMove = analysisModeUseCase.currentMove
                    )
                }
            }
        )
    }

    override fun onDetachedView() {
        gameModeUseCase.stop()
        chessEngineService.stop()
        super.onDetachedView()
    }

    override fun onActionReceived(action: GameScreenAction) {
        when (action) {
            is GameScreenAction.MoveListResizeButtonClicked -> {
                onExpandMoveListClicked(action.expand)
            }
            is GameScreenAction.ReplayControlButtonClicked -> {
                onReplayControlButtonClicked(action.item)
            }
            is GameScreenAction.MoveListItemClicked -> {
                onMoveHistoryItemClicked(action.item, action.player)
            }
        }
    }

    fun initializePlayers(playerIds: PlayerMap<Int>) {
        this.playerIds = playerIds
    }

    private fun switchToAnalysisMode() {
        gameModeUseCase.stop()
        isInAnalysisMode = true
        onModeChanged()
    }

    private fun switchToGameMode() {
        isInAnalysisMode = false
        gameModeUseCase.start()
        onModeChanged()
    }

    private fun onModeChanged() {
        viewModel.currentState = currentState.copy(
            replayControlItems = getBottomBarButtons()
        )
    }

    private fun onReplayControlButtonClicked(item: ReplayGameControlItem) {
        when (item) {
            ReplayGameControlItem.Pause -> switchToAnalysisMode()
            ReplayGameControlItem.Play -> switchToGameMode()
        }
    }

    private fun onExpandMoveListClicked(expand: Boolean) {
        val currentState = currentState
        viewModel.currentState = currentState.copy(
            moveListCollapsed = !expand
        )
    }

    private fun onMoveHistoryItemClicked(item: MoveListItem, player: Player) {
        android.util.Log.w("TXL", "Marker 1")
        if (!isInAnalysisMode) {
            return
        }
        android.util.Log.w("TXL", "Marker 2")
        val selectedHistoryMove = item.getMoveItem(player)?.detailedMove ?: return
        analysisModeUseCase.goBackToMove(
            selectedHistoryMove,
            currentState.chessEngineState.moveHistory
        )
    }

    private fun getBottomBarButtons(): List<ReplayGameControlItem> {
        return if (isInAnalysisMode) {
            ANALYSIS_MODE_BOTTOM_BAR_BUTTONS
        } else {
            GAME_MODE_BOTTOM_BAR_BUTTONS
        }
    }

    private fun getPerson(player: Player): Person {
        val personId = playerIds.get(player)
        return personsRepository.findPerson(personId) ?: personsRepository.getHuman()
    }

    private companion object {
        private val GAME_MODE_BOTTOM_BAR_BUTTONS = listOf(
            ReplayGameControlItem.Pause
        )

        private val ANALYSIS_MODE_BOTTOM_BAR_BUTTONS = listOf(
            ReplayGameControlItem.Play
        )
    }
}
