package ru.maksimbulva.chess

import android.app.Application
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module
import ru.maksimbulva.chess.person.PersonsRepository

class ChessApplication : Application() {

    private val appModule = module {
        single { PersonsRepository() }
        single { ChessEngineService() }
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            modules(appModule)
        }
    }
}
