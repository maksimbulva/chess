package ru.maksimbulva.chess.screens.game

import ru.maksimbulva.chess.core.engine.Player
import ru.maksimbulva.ui.move_list.items.MoveListItem
import ru.maksimbulva.ui.replay.items.ReplayGameControlItem

sealed class GameScreenAction {
    class MoveListResizeButtonClicked(val expand: Boolean) : GameScreenAction()
    class ReplayControlButtonClicked(val item: ReplayGameControlItem) : GameScreenAction()
    class MoveListItemClicked(val item: MoveListItem, val player: Player) : GameScreenAction()
}
