package ru.maksimbulva.chess.screens.game

import android.content.res.Resources
import ru.maksimbulva.chess.core.engine.Player
import ru.maksimbulva.chess.core.engine.move.DetailedMove
import ru.maksimbulva.chess.settings.UserSettings
import ru.maksimbulva.ui.move_list.items.MoveListItem
import ru.maksimbulva.ui.move_list.items.PlayerMoveListItem

class MoveListItemsGenerator(private val userSettings: UserSettings) {

    fun generateMoveListItems(
        resources: Resources,
        moveHistory: List<DetailedMove>,
        selectedHistoryMove: DetailedMove?
    ): List<MoveListItem> {
        val moveItems = mutableListOf<MoveListItem>()

        var i = 0
        while (i < moveHistory.size) {
            val playerMoves = listOfNotNull(
                moveHistory[i],
                if (i + 1 < moveHistory.size &&
                    moveHistory[i + 1].fullmoveNumber == moveHistory[i].fullmoveNumber) {
                    moveHistory[i + 1]
                } else {
                    null
                }
            )

            val blackMove = playerMoves.find { it.playerToMove == Player.Black }
            val whiteMove = playerMoves.find { it.playerToMove == Player.White }

            val selectedPlayerMove = when (selectedHistoryMove) {
                null -> null
                blackMove -> Player.Black
                whiteMove -> Player.White
                else -> null
            }

            moveItems.add(
                MoveListItem(
                    moveNumberText = moveHistory[i].fullmoveNumber.toString(),
                    selectedPlayerMove = selectedPlayerMove,
                    blackMoveItem = blackMove?.createPlayerMoveListItem(resources),
                    whiteMoveItem = whiteMove?.createPlayerMoveListItem(resources)
                )
            )

            i += playerMoves.size
        }

        return moveItems
    }

    private fun DetailedMove?.createPlayerMoveListItem(resources: Resources): PlayerMoveListItem? {
        return if (this != null) {
            PlayerMoveListItem(
                detailedMove = this,
                moveText = userSettings.notation.moveToString(resources, this),
                isSelected = false
            )
        } else {
            null
        }
    }
}
