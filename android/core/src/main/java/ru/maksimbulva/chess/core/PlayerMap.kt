package ru.maksimbulva.chess.core

import ru.maksimbulva.chess.core.engine.Player

class PlayerMap<V>(
    val blackPlayerValue: V,
    val whitePlayerValue: V
) {
    fun get(player: Player) = when (player) {
        Player.Black -> blackPlayerValue
        Player.White -> whitePlayerValue
    }

    fun copyWith(player: Player, value: V): PlayerMap<V> {
        return when (player) {
            Player.Black -> PlayerMap(value, whitePlayerValue)
            Player.White -> PlayerMap(blackPlayerValue, value)
        }
    }
}
