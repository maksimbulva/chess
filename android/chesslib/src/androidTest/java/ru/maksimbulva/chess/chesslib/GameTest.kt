package ru.maksimbulva.chess.chesslib

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.junit.runner.RunWith
import ru.maksimbulva.chess.core.engine.Engine

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
}
