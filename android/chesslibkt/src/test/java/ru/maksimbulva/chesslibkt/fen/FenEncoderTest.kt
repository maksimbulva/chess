package ru.maksimbulva.chesslibkt.fen

import org.junit.Assert.assertEquals
import org.junit.Test
import ru.maksimbulva.chesslibkt.ChessEngineFactory
import ru.maksimbulva.chesslibkt.Fen

internal class FenEncoderTest {

    private val engine = ChessEngineFactory.createInstance()

    @Test
    fun encodeInitialPositionTest() {
        engine.resetGame()
        val encoded = Fen.encodeFen(engine.game.currentPosition)
        assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", encoded)
    }

    @Test
    fun encodePositionAfterFirstMove() {
        engine.resetGame()
        engine.playMove("e2e4")
        val encoded = Fen.encodeFen(engine.game.currentPosition)
        assertEquals("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1", encoded)
    }
}
