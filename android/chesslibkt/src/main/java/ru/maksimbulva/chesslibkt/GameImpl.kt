package ru.maksimbulva.chesslibkt

import ru.maksimbulva.chesslibkt.move.Move
import ru.maksimbulva.chesslibkt.move.MoveGenerationFilter
import ru.maksimbulva.chesslibkt.position.Position

class GameImpl(position: Position) : IGame {
    private var _initialPosition: Position = position
    private var _currentPosition: Position = position

    override val currentPosition: Position
        get() = _currentPosition

    private var legalMoves = emptyList<Move>()

    init {
        updateLegalMoves()
    }

    override fun getLegalMoves(): List<Move> = legalMoves

    @ExperimentalStdlibApi
    override fun getRandomMove(): Move? = legalMoves.randomOrNull()

    override fun playMove(move: Move) {
        TODO("Not yet implemented")
    }

    private fun updateLegalMoves()
    {
        val newLegalMoves = mutableListOf<Move>()

        val pseudoLegalMoves = _currentPosition.generatePseudoLegalMoves(
            MoveGenerationFilter.AllMoves
        )

        val tmpPosition = _currentPosition.clone()

        // TODO: optimize me
        pseudoLegalMoves.forEach { scoredMove ->
            tmpPosition.playMove(scoredMove.move)
            if (tmpPosition.isValid()) {
                newLegalMoves.add(scoredMove.move)
            }
            tmpPosition.undoMove()
        }

        legalMoves = newLegalMoves
    }
}
