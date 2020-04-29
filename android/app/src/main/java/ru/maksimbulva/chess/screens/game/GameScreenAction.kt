package ru.maksimbulva.chess.screens.game

import ru.maksimbulva.ui.replay.items.ReplayGameControlItem

sealed class GameScreenAction {
    object ExpandMoveListClicked : GameScreenAction()
    class ReplayControlButtonClicked(val item: ReplayGameControlItem) : GameScreenAction()
}
