package ru.maksimbulva.chesslibkt

import ru.maksimbulva.chesslibkt.board.Square
import ru.maksimbulva.chesslibkt.move.ParsedMove
import ru.maksimbulva.chesslibkt.search.SearchInfo
import ru.maksimbulva.chesslibkt.search.Variation
import kotlin.Exception

class ChessEngineImpl : IChessEngine {
    private val playerSettings = mutableMapOf(
        Player.Black to PlayerSettings(),
        Player.White to PlayerSettings()
    )

    override val name: String = NAME

    private var _game = GameImpl(Fen.decodeFen(INITIAL_POSITION_FEN))

    override val game: IGame
        get() = _game

    override val searchInfo: SearchInfo
        get() = TODO("Not yet implemented")

    override fun getPlayerSettings(player: Player): PlayerSettings {
        return playerSettings[player]!!
    }

    override fun resetGame() = resetGame(INITIAL_POSITION_FEN)

    override fun resetGame(positionFen: String) {
        _game = GameImpl(Fen.decodeFen(positionFen))
    }

    override fun playMove(
        originSquare: Square,
        destSquare: Square,
        promoteToPieceType: Piece?
    ): Boolean {
        val moveToPlay = _game.getLegalMoves().firstOrNull { move ->
            move.originSquare == originSquare &&
                    move.destSquare == destSquare &&
                    ((move.isPromotion && move.promoteToPiece == promoteToPieceType)
                            || (!move.isPromotion && promoteToPieceType == null))
        }

        return if (moveToPlay != null) {
            _game.playMove(moveToPlay)
            true
        } else {
            false
        }
    }

    override fun playMove(moveString: String): Boolean {
        return try {
            val parsedMove = ParsedMove.fromCoordinateNotation(moveString)
            playMove(parsedMove.originSquare, parsedMove.destSquare, parsedMove.promoteToPieceType)
        } catch (e: Exception) {
            false
        }
    }

    override fun findBestVariation(progressCallback: (SearchInfo) -> Unit): Variation {
        TODO("Not yet implemented")
    }

    companion object {
        private const val NAME = "ChesslibKt v0.1"

        private const val INITIAL_POSITION_FEN =
            "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"
    }
}
