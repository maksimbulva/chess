package ru.maksimbulva.chess.chess.command

import ru.maksimbulva.chess.chesslib.ChesslibWrapper
import ru.maksimbulva.chess.core.PlayerMap
import ru.maksimbulva.chess.core.engine.Player
import ru.maksimbulva.chess.person.Person

internal class SetPlayersCommand(
    val players: PlayerMap<Person>,
    private val chesslibWrapper: ChesslibWrapper
) : ChessEngineCommand() {

    override fun execute(onComplete: () -> Unit) {
        Player.values()
            .filter { players.get(it) is Person.Computer }
            .forEach { configureComputerPlayer(it, players.get(it) as Person.Computer) }
        onComplete()
    }

    private fun configureComputerPlayer(player: Player, person: Person.Computer) {
        with (chesslibWrapper.getPlayer(player)) {
            setPlayerEvaluationsLimit(person.evaluationsLimit)
            setDegreeOfRandomness(person.degreeOfRandomness)
        }
    }
}
