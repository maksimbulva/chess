package ru.maksimbulva.chesslibkt

import ru.maksimbulva.chesslibkt.move.Move
import ru.maksimbulva.chesslibkt.move.MoveGenerationFilter
import ru.maksimbulva.chesslibkt.position.Position

class GameImpl(position: Position) : IGame {
    private var _initialPosition: Position = position
    private var _currentPosition: Position = position

    override val currentPosition: Position
        get() = _currentPosition

    private var legalMoves: List<Move>? = null

    override fun getLegalMoves(): List<Move> {
        if (legalMoves == null) {
            updateLegalMoves()
        }
        return legalMoves!!
    }

    override fun playMove(move: Move) {
        _currentPosition.playMove(move)
        _currentPosition.updateMoveCounters(move)
        legalMoves = null
    }

    override fun undoMove() {
        _currentPosition.undoMove()
        legalMoves = null
    }

    private fun updateLegalMoves() {
        val newLegalMoves = mutableListOf<Move>()

        val pseudoLegalMoves = _currentPosition.generatePseudoLegalMoves(
            MoveGenerationFilter.AllMoves
        )

        val tmpPosition = _currentPosition.clone()

        // TODO: optimize me
        pseudoLegalMoves.forEach { move ->
            tmpPosition.playMove(move)
            if (tmpPosition.isValid()) {
                newLegalMoves.add(move)
            }
            tmpPosition.undoMove()
        }

        legalMoves = newLegalMoves
    }
}
