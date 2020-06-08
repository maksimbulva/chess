package ru.maksimbulva.chesslibkt

import ru.maksimbulva.chesslibkt.board.Square
import ru.maksimbulva.chesslibkt.search.SearchInfo
import ru.maksimbulva.chesslibkt.search.Variation

interface IChessEngine {
    val name: String

    val game: IGame

    val searchInfo: SearchInfo

    fun getPlayerSettings(player: Player): PlayerSettings

    fun resetGame()

    fun resetGame(positionFen: String)

    fun playMove(
        originSquare: Square,
        destSquare: Square,
        promoteToPieceType: Piece?): Boolean

    fun playMove(moveString: String): Boolean

    fun findBestVariation(progressCallback: (SearchInfo) -> Unit): Variation
}
