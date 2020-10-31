package ru.maksimbulva.chess.screens.game_setup

import ru.maksimbulva.chess.core.engine.Player

sealed class GameSetupScreenAction {
    class ReplacePlayerButtonClicked(val player: Player) : GameSetupScreenAction()
    class SelectPerson(val personId: Int?): GameSetupScreenAction()
    object StartGameButtonClicked : GameSetupScreenAction()
}
