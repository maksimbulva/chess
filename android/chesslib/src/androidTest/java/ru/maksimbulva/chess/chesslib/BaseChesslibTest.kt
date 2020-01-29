package ru.maksimbulva.chess.chesslib

import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import ru.maksimbulva.chess.core.engine.move.Move

abstract class BaseChesslibTest {

    protected lateinit var chesslibWrapper: ChesslibWrapper

    @Before
    fun setUp() {
        chesslibWrapper = ChesslibWrapper()
    }

    @After
    fun tearDown() {
        chesslibWrapper.destroy()
    }

    protected fun assertBestMove(fen: String, expectedBestMove: Move) {
        chesslibWrapper.resetGame(fen)
        val actualVariation = chesslibWrapper.findBestVariation(onlyFirstMove = true)
        assertEquals(expectedBestMove, actualVariation.moves.first())
    }
}
