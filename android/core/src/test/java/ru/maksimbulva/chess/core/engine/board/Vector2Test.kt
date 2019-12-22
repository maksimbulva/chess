package ru.maksimbulva.chess.core.engine.board

import org.junit.Test
import org.junit.Assert.*
import ru.maksimbulva.chess.core.engine.Player

internal class Vector2Test {

    @Test
    fun isPawnAttackTest() {
        with(Cell.of("g1") - Cell.of("f2")) {
            assertTrue(isPawnAttack(Player.Black))
            assertFalse(isPawnAttack(Player.White))
        }

        with(Cell.of("d5") - Cell.of("e4")) {
            assertTrue(isPawnAttack(Player.White))
            assertFalse(isPawnAttack(Player.Black))
        }

        with(Cell.of("f3") - Cell.of("g1")) {
            assertFalse(isPawnAttack(Player.Black))
            assertFalse(isPawnAttack(Player.White))
        }
    }
}
