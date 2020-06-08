package ru.maksimbulva.chesslibkt.position

inline class CastleOptions(val value: Int) {

    val isCannotCastle: Boolean
        get() = value == 0

    val isCanCastleShort: Boolean
        get() = (value and CAN_CASTLE_SHORT) != 0

    val isCanCastleLong: Boolean
        get() = (value and CAN_CASTLE_LONG) != 0

    fun setCanCastleShort(canCastleShort: Boolean): CastleOptions {
        return setCanCastle(canCastleShort, CAN_CASTLE_SHORT)
    }

    fun setCanCastleLong(canCastleLong: Boolean): CastleOptions {
        return setCanCastle(canCastleLong, CAN_CASTLE_LONG)
    }

    private fun setCanCastle(canCastle: Boolean, flag: Int): CastleOptions {
        return CastleOptions(
            if (canCastle) {
                value or flag
            } else {
                value or flag.inv()
            }
        )
    }

    companion object {
        private const val CAN_CASTLE_SHORT = 1
        private const val CAN_CASTLE_LONG = 2
        private const val MAX_ENCODED_VALUE = 3

        val None = CastleOptions(0)
    }
}
