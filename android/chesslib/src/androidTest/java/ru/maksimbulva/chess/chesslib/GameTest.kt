package ru.maksimbulva.chess.chesslib

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.junit.runner.RunWith
import ru.maksimbulva.chess.core.engine.Engine
import ru.maksimbulva.chess.core.engine.board.Cell
import ru.maksimbulva.chess.core.engine.move.Move

@RunWith(AndroidJUnit4::class)
class GameTest {

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
    fun resetSetsInitialPositionTest() {
        chesslibWrapper.resetGame()
        assertEquals(Engine.initialPosition, chesslibWrapper.currentPositionFen())
    }

    @Test
    fun positionAfterFewMovesPlayedTest() {
        chesslibWrapper.resetGame()
        chesslibWrapper.playMove(Move(Cell.of("e2"), Cell.of("e4")))
        chesslibWrapper.playMove(Move(Cell.of("d7"), Cell.of("d5")))
        chesslibWrapper.playMove(Move(Cell.of("e4"), Cell.of("d5")))
        chesslibWrapper.playMove(Move(Cell.of("d8"), Cell.of("d5")))
        chesslibWrapper.playMove(Move(Cell.of("b1"), Cell.of("c3")))
        assertEquals(
            "rnb1kbnr/ppp1pppp/8/3q4/8/2N5/PPPP1PPP/R1BQKBNR b KQkq - 1 3",
            chesslibWrapper.currentPositionFen()
        )
    }
}
