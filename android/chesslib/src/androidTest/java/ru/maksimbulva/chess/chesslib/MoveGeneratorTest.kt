package ru.maksimbulva.chess.chesslib

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MoveGeneratorTest {

    @Test
    fun moveCountFromInitialPositionTest() {
        assertMoveCount(
            "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1",
            arrayOf(20L, 400L, 8_902L, 197_281L, 4_865_609L)
        )
    }

    @Test
    fun moveCountFromKiwipetePositionTest() {
        assertMoveCount(
            "r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq -",
            arrayOf(48L, 2_039L, 97_862L, 4_085_603L)
        )
    }

    @Test
    fun moveCountInEndgamePositionTest() {
        assertMoveCount(
            "8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - -",
            arrayOf(14L, 191L, 2_812L, 43_238L, 674_624L)
        )
    }

    @Test
    fun moveCountForStevenEdwardsPositionTest() {
        assertMoveCount(
            "r4rk1/1pp1qppp/p1np1n2/2b1p1B1/2B1P1b1/P1NP1N2/1PP1QPPP/R4RK1 w - - 0 10",
            arrayOf(46L, 2_079L, 89_890L, 3_894_594L)
        )
    }

    @Test
    fun moveCountForSharpOpeningPositionTest() {
        assertMoveCount(
            "rnbq1k1r/pp1Pbppp/2p5/8/2B5/8/PPP1NnPP/RNBQK2R w KQ - 1 8",
            arrayOf(44L, 1_486L, 62_379L, 2_103_487L)
        )
    }

    private fun assertMoveCount(fenString: String, expectedMoveCount: Array<Long>) {
        val chesslibWrapper = ChesslibWrapper()
        expectedMoveCount.forEachIndexed { index, expected ->
            val depthPly = index + 1
            assertEquals(expected, chesslibWrapper.calculateLegalMovesCount(fenString, depthPly))
        }
        chesslibWrapper.destroy()
    }
}
