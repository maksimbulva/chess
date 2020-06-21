package ru.maksimbulva.chesslibkt.move

import org.junit.Test
import org.junit.Assert.*
import ru.maksimbulva.chesslibkt.Fen
import ru.maksimbulva.chesslibkt.GameImpl
import ru.maksimbulva.chesslibkt.IGame
import ru.maksimbulva.chesslibkt.position.Position

internal class MoveGeneratorTest {

    @Test
    fun moveCountFromInitialPositionTest() {
        val game = GameImpl(Fen.decodeFen(
            "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"
        ))
        assertEquals(20L, countMoves(game, 1))
        assertEquals(400L, countMoves(game, 2))
//        assertEquals(8_902L, countMoves(initialPosition, 3))
//        assertEquals(197_281L, countMoves(initialPosition, 4))
//        assertEquals(4_865_609L, countMoves(initialPosition, 5))
    }
/*
    @Test
    fun moveCountFromKiwipetePositionTest() {
        val position = FenDecoder.decode("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq -")
        assertEquals(48L, countMoves(position, 1))
        assertEquals(2_039L, countMoves(position, 2))
        assertEquals(97_862L, countMoves(position, 3))
        assertEquals(4_085_603L, countMoves(position, 4))
    }

    @Test
    fun moveCountInEndgamePositionTest() {
        val position = FenDecoder.decode("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - -")
        assertEquals(14L, countMoves(position, 1))
        assertEquals(191L, countMoves(position, 2))
        assertEquals(2_812L, countMoves(position, 3))
        assertEquals(43_238L, countMoves(position, 4))
        assertEquals(674_624L, countMoves(position, 5))
    }

    @Test
    fun moveCountForStevenEdwardsPositionTest() {
        val position = FenDecoder.decode(
            "r4rk1/1pp1qppp/p1np1n2/2b1p1B1/2B1P1b1/P1NP1N2/1PP1QPPP/R4RK1 w - - 0 10"
        )

        assertEquals(46L, countMoves(position, 1))
        assertEquals(2_079L, countMoves(position, 2))
        assertEquals(89_890L, countMoves(position, 3))
        assertEquals(3_894_594L, countMoves(position, 4))
    }

    @Test
    fun moveCountForSharpOpeningPositionTest() {
        val position = FenDecoder.decode("rnbq1k1r/pp1Pbppp/2p5/8/2B5/8/PPP1NnPP/RNBQK2R w KQ - 1 8")
        assertEquals(44L, countMoves(position, 1))
        assertEquals(1_486L, countMoves(position, 2))
        assertEquals(62_379L, countMoves(position, 3))
        assertEquals(2_103_487L, countMoves(position, 4))
    }
*/
    private fun countMoves(game: IGame, depth: Int): Long {
        return when {
            depth > 1 -> {
                val legalMoves = game.getLegalMoves().toList()
                legalMoves.asSequence()
                    .map {
                        game.playMove(it)
                        val result = countMoves(game, depth - 1)
                        game.undoMove()
                        result
                    }
                    .sum()
            }
            depth == 1 -> game.getLegalMoves().size.toLong()
            else -> 0L
        }
    }
}
