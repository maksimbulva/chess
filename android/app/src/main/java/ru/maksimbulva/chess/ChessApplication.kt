package ru.maksimbulva.chess

import android.app.Application
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module
import ru.maksimbulva.chess.chess.ChessEngineService
import ru.maksimbulva.chess.chess.notation.LongAlgebraicNotation
import ru.maksimbulva.chess.person.PersonsRepository
import ru.maksimbulva.chess.screens.game.GameScreenInteractor
import ru.maksimbulva.chess.settings.UserSettings

class ChessApplication : Application() {

    private val appModule = module {
        single { PersonsRepository() }
        single { ChessEngineService() }
        single { GameScreenInteractor(get()) }
        single { UserSettings(LongAlgebraicNotation) }
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            modules(appModule)
        }
    }
}
