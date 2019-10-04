package chess.engine.core.board

import chess.engine.core.Player
import kotlin.math.abs
import kotlin.math.sign

class Vector2(val deltaRow: Int, val deltaColumn: Int) {

    fun isPawnAttack(attacker: Player): Boolean {
        if (deltaColumn == -1 || deltaColumn == 1) {
            return when (attacker) {
                Player.Black -> deltaRow == -1
                Player.White -> deltaRow == 1
            }
        }
        return false
    }

    fun isKnightAttack(): Boolean {
        val absDeltaRow = abs(deltaRow)
        val absDeltaColumn = abs(deltaColumn)
        return ((absDeltaRow == 2 && absDeltaColumn == 1)
                || (absDeltaRow == 1 && absDeltaColumn == 2))
    }

    fun isBishopAttack(): Boolean {
        return (deltaRow == deltaColumn) || (deltaRow == -deltaColumn)
    }

    fun isRookAttack(): Boolean {
        return deltaRow == 0 || deltaColumn == 0
    }

    fun isQueenAttack(): Boolean {
        return isBishopAttack() || isRookAttack()
    }

    fun isKingAttack(): Boolean {
        return (deltaRow in -1..1) && (deltaColumn in -1..1)
    }

    fun toUnitLength(): Vector2 {
        return Vector2(deltaRow.sign, deltaColumn.sign)
    }
}
