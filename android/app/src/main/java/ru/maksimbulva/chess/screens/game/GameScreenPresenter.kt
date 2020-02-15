package ru.maksimbulva.chess.screens.game

import ru.maksimbulva.chess.chess.ChessEngineService
import ru.maksimbulva.chess.mvp.BasePresenter
import ru.maksimbulva.chess.person.PersonsRepository

class GameScreenPresenter(
    private val chessEngineService: ChessEngineService,
    private val personsRepository: PersonsRepository,
    private val interactor: GameScreenInteractor
) : BasePresenter<IGameScreenView, GameScreenViewModel>() {

    override fun onAttachedView(view: IGameScreenView) {
        super.onAttachedView(view)

        chessEngineService.setPlayers(
            personSideWhite = personsRepository.alice,
            personSideBlack = personsRepository.bob
        )

        addSubscription(
            chessEngineService.currentPosition.subscribe {
                viewModel.setPosition(it)
                interactor.onPositionChanged()
            }
        )

        addSubscription(
            chessEngineService.bestVariation.subscribe { bestVariationByPlayer ->
                bestVariationByPlayer.forEach {
                    viewModel.updateBestVariation(it.key, it.value)
                }
            }
        )

        view.setPlayMoveOnClickListener {
            addSubscription(
                chessEngineService.playBestMoveAsync().subscribe()
            )
        }
    }

    override fun onDetachedView() {
        super.onDetachedView()
        interactor.stop()
    }
}
