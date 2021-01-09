package ru.maksimbulva.chess.core.engine

inline class Bitmask64(val value: Long) {

    fun setBit(index: Int): Bitmask64 {
        return Bitmask64(value or (1L shl index))
    }

    operator fun get(index: Int): Boolean {
        return (value and (1L shl index)) != 0L
    }
}
