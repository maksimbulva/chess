package chess.engine.fen

import chess.engine.Engine
import chess.engine.core.board.Cell
import chess.engine.core.move.Move
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class FenEncoderTest {

    private val engine = Engine()

    @Test
    fun encodeInitialPositionTest() {
        engine.resetToInitialPosition()
        val encoded = FenEncoder.encode(engine.currentPosition, FenEncoder.EncodingOptions.None)
        assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", encoded)
    }

    @Test
    fun encodePositionAfterFirstMove() {
        engine.resetToInitialPosition()
        engine.playMove(Move(fromCell = Cell.of("e2"), toCell = Cell.of("e4")))
        val encoded = FenEncoder.encode(engine.currentPosition, FenEncoder.EncodingOptions.None)
        assertEquals("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1", encoded)
    }
}