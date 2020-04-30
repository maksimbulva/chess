package ru.maksimbulva.ui.move_list.items

import ru.maksimbulva.chess.core.engine.Player
import ru.maksimbulva.chess.core.engine.move.DetailedMove

data class PlayerMoveListItem(
    val detailedMove: DetailedMove,
    val moveText: String,
    val isSelected: Boolean
)

data class MoveListItem(
    val moveNumberText: String,
    val selectedPlayerMove: Player?,
    val blackMoveItem: PlayerMoveListItem?,
    val whiteMoveItem: PlayerMoveListItem?
) {
    fun getMoveItem(player: Player): PlayerMoveListItem? {
        return when (player) {
            Player.Black -> blackMoveItem
            Player.White -> whiteMoveItem
        }
    }
}
