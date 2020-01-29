package ru.maksimbulva.chess.chesslib

import ru.maksimbulva.chess.core.engine.Player

class ChesslibPlayerWrapper(
    private val chesslibWrapper: ChesslibWrapper,
    private val player: Player
) {
    fun setPlayerEvaluationsLimit(evaluationsLimit: Long) {
        chesslibWrapper.setPlayerEvaluationsLimit(player, evaluationsLimit)
    }
}
