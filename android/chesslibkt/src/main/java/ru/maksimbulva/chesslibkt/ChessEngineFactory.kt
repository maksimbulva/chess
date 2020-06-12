package ru.maksimbulva.chesslibkt

object ChessEngineFactory {

    fun createInstance(): IChessEngine {
        return ChessEngineImpl()
    }
}
