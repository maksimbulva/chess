package chess.engine

import chess.engine.core.move.Move
import chess.engine.core.move.MoveGenerator
import chess.engine.core.position.Position
import chess.engine.fen.FenDecoder

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
        private const val initialPosition = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"
    }
}