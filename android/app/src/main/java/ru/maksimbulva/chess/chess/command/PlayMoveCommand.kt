package ru.maksimbulva.chess.chess.command

import ru.maksimbulva.chess.chesslib.ChesslibWrapper
import ru.maksimbulva.chess.core.engine.Engine
import ru.maksimbulva.chess.core.engine.move.Move

internal class PlayMoveCommand(
    private val move: Move,
    private val engine: Engine,
    private val chesslibWrapper: ChesslibWrapper
) : ChessEngineCommand() {

    override fun execute(onComplete: () -> Unit) {
        engine.playMove(move)
        chesslibWrapper.playMove(move)
        onComplete()
    }
}
