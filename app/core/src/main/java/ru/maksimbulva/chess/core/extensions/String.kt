package ru.maksimbulva.chess.core.extensions

fun String.toUpperCaseIf(predicate: () -> Boolean): String {
    return if (predicate()) {
        toUpperCase()
    } else {
        this
    }
}
