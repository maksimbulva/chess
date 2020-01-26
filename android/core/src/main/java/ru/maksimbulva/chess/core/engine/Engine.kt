package ru.maksimbulva.chess.core.engine

import ru.maksimbulva.chess.core.engine.fen.FenDecoder
import ru.maksimbulva.chess.core.engine.move.Move
import ru.maksimbulva.chess.core.engine.move.MoveGenerator
import ru.maksimbulva.chess.core.engine.position.Position

class Engine {

    var currentPosition: Position = FenDecoder.decode(initialPosition)
        private set

    var moveHistory: List<Move> = emptyList()

    private var _legalMoves: List<Move>? = null
    val legalMoves: List<Move>
        get() {
            if (_legalMoves == null) {
                _legalMoves = MoveGenerator.generateMoves(currentPosition)
            }
            return _legalMoves!!
        }

    fun resetToInitialPosition() {
        currentPosition = FenDecoder.decode(initialPosition)
    }

    fun playMove(move: Move) {
        require(move in legalMoves)
        currentPosition = currentPosition.playMove(move)
        moveHistory = mutableListOf<Move>().apply {
            addAll(moveHistory)
            add(move)
        }
        _legalMoves = null
    }

    companion object {
        const val initialPosition = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"
    }
}