package ru.maksimbulva.chess.screens.game

import android.content.res.Resources
import ru.maksimbulva.chess.R
import ru.maksimbulva.chess.core.engine.Player
import ru.maksimbulva.chess.core.engine.move.DetailedMove
import ru.maksimbulva.chess.settings.UserSettings
import ru.maksimbulva.ui.move_list.items.MoveListItem

class MoveListItemsGenerator(private val userSettings: UserSettings) {

    fun generateMoveListItems(
        resources: Resources,
        moveHistory: List<DetailedMove>
    ): List<MoveListItem> {
        val result = mutableMapOf<Int, MoveListItem>()
        moveHistory.map {
            var item = result.get(it.fullmoveNumber) ?: MoveListItem(
                moveNumberText = resources.getString(
                    R.string.move_list_move_number_template,
                    it.fullmoveNumber.toString()
                ),
                whiteMoveText = null,
                blackMoveText = null
            )

            val moveText = userSettings.notation.moveToString(resources, it)
            item = when (it.playerToMove) {
                Player.Black -> item.copy(blackMoveText = moveText)
                Player.White -> item.copy(whiteMoveText = moveText)
            }

            result[it.fullmoveNumber] = item
        }
        return result.values.toList()
    }
}
