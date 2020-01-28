package ru.maksimbulva.chess.chesslib

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.junit.runner.RunWith
import ru.maksimbulva.chess.core.engine.board.Cell
import ru.maksimbulva.chess.core.engine.move.Move

@RunWith(AndroidJUnit4::class)
class FindMateTest {

    private lateinit var chesslibWrapper: ChesslibWrapper

    @Before
    fun setUp() {
        chesslibWrapper = ChesslibWrapper()
    }

    @After
    fun tearDown() {
        chesslibWrapper.destroy()
    }

    @Test
    fun findMateInTwoTest() {
        assertBestMove(
            fen = "2bqkbn1/2pppp2/np2N3/r3P1p1/p2N2B1/5Q2/PPPPKPP1/RNB2r2 w KQkq - 0 1",
            expectedBestMove = Move(Cell.of("f3"), Cell.of("f7"))
        )

        assertBestMove(
            fen = "8/6K1/1p1B1RB1/8/2Q5/2n1kP1N/3b4/4n3 w - - 0 1",
            expectedBestMove = Move(Cell.of("d6"), Cell.of("a3"))
        )

        assertBestMove(
            fen = "B7/K1B1p1Q1/5r2/7p/1P1kp1bR/3P3R/1P1NP3/2n5 w - - 0 1",
            expectedBestMove = Move(Cell.of("a8"), Cell.of("c6"))
        )
    }

    private fun assertBestMove(fen: String, expectedBestMove: Move) {
        chesslibWrapper.resetGame(fen)
        val actualVariation = chesslibWrapper.findBestVariation(onlyFirstMove = true)
        assertEquals(expectedBestMove, actualVariation.moves.first())
    }
}
