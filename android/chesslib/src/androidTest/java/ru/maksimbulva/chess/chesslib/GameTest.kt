package ru.maksimbulva.chess.chesslib

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.Assert.*
import org.junit.runner.RunWith
import ru.maksimbulva.chess.core.engine.Engine
import ru.maksimbulva.chess.core.engine.board.Cell
import ru.maksimbulva.chess.core.engine.move.Move

@RunWith(AndroidJUnit4::class)
class GameTest : BaseChesslibTest() {

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

    @Test
    fun resetToSpecificPositionTest() {
        chesslibWrapper.resetGame("2bqkbn1/2pppp2/np2N3/r3P1p1/p2N2B1/5Q2/PPPPKPP1/RNB2r2 w KQkq - 0 1")
        chesslibWrapper.playMove(Move(Cell.of("e6"), Cell.of("c7")))
        assertEquals(
            "2bqkbn1/2Nppp2/np6/r3P1p1/p2N2B1/5Q2/PPPPKPP1/RNB2r2 b - - 0 1",
            chesslibWrapper.currentPositionFen()
        )

    }
}
