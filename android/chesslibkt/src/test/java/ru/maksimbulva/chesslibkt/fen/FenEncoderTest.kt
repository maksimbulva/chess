package ru.maksimbulva.chesslibkt.fen

import org.junit.Assert
import org.junit.Test
import ru.maksimbulva.chesslibkt.ChessEngineFactory
import ru.maksimbulva.chesslibkt.Fen

internal class FenEncoderTest {

    private val engine = ChessEngineFactory.createInstance()

    @Test
    fun encodeInitialPositionTest() {
        engine.resetGame()
        val encoded = Fen.encodeFen(engine.game.currentPosition)
        Assert.assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", encoded)
    }
}
