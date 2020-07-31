package ru.maksimbulva.chess.chess.command

internal abstract class ChessEngineCommand {
    abstract fun execute(onComplete: () -> Unit)

    open fun abort() {
    }
}
